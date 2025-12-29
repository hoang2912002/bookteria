package com.hamet.identity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hamet.eventDto.NotificationEvent;
import com.hamet.identity.constant.PredefinedRole;
import com.hamet.identity.dto.request.ApiResponse;
import com.hamet.identity.dto.request.UserCreationRequest;
import com.hamet.identity.dto.request.UserUpdateRequest;
import com.hamet.identity.dto.response.RoleResponse;
import com.hamet.identity.dto.response.UserProfileResponse;
import com.hamet.identity.dto.response.UserResponse;
import com.hamet.identity.entity.Role;
import com.hamet.identity.entity.User;
import com.hamet.identity.exception.AppException;
import com.hamet.identity.exception.ErrorCode;
import com.hamet.identity.mapper.ProfileMapper;
import com.hamet.identity.mapper.UserMapper;
import com.hamet.identity.repository.RoleRepository;
import com.hamet.identity.repository.UserRepository;
import com.hamet.identity.repository.httpClient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user = userRepository.save(user);

        var profileRequest = profileMapper.toProfileCreateRequest(request); // var = tự suy luận DataType
        profileRequest.setUserId(user.getId());
        val profileResponse = profileClient.createProfile(profileRequest); // var + final

        NotificationEvent notificationEvent = NotificationEvent.builder()
            .channel("EMAIL")
            .recipient(request.getFirstName() + request.getLastName())
            .subject("Welcome to bookteria")
            .body("Hello")
            .build();

        // Publish message to kafka đăng ký user gửi mail
        kafkaTemplate.send("notification-delivery", notificationEvent)
            .whenComplete((result, ex) -> {
                if (ex == null) log.info("Sent message thành công: {}", result.getRecordMetadata().offset());
                else log.error("Gửi lỗi: {}", ex.getMessage());
            });;
        return userMapper.toUserResponse(user);
    }

    public UserProfileResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        UserProfileResponse profile = profileClient.getProfile(user.getId()).getResult();
        profile.setEmail(user.getEmail());
        profile.setEmailVerified(user.isEmailVerified());
        profile.setUserId(user.getId());

        // Mapping từ Set<Role> sang Set<RoleResponse>
        Set<RoleResponse> roleResponses = user.getRoles().stream()
        .map(role -> RoleResponse.builder()
            .name(role.getName())
            .description(role.getDescription())
            // map các trường khác nếu có
            .build())
        .collect(Collectors.toSet());
        profile.setRoles(roleResponses);
        return profile;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
