package com.hamet.profile.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE) 
@Builder
public class UserProfileResponse {
    String id;
    String userId;
    String username;
    String firstName;
    String lastName;
    String city;
    LocalDate dob;
    String avatar;
}
