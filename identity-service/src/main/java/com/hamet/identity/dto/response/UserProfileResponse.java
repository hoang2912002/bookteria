package com.hamet.identity.dto.response;

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
    String firstName;
    String lastName;
    String city;
    LocalDate dob;
}
