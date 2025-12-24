package com.hamet.identity.dto.request;

import java.time.LocalDate;

import com.hamet.identity.validator.DobConstraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileCreateRequest {
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
}
