package com.hamet.chat.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.hamet.chat.dto.request.IntrospectRequest;
import com.hamet.chat.dto.response.IntrospectResponse;
import com.hamet.chat.repository.httpClient.IdentityClient;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public IntrospectResponse introspect(IntrospectRequest request){
        try {
            IntrospectResponse result = identityClient.getProfile(request).getResult();
            if(Objects.isNull(result)){
                return IntrospectResponse.builder()
                .valid(false)
                .build();
            }
            return result;
        } catch (FeignException e){
            return IntrospectResponse.builder()
                .valid(false)
                .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
