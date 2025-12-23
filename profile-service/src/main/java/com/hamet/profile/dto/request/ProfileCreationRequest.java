package com.hamet.profile.dto.request;

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
public class ProfileCreationRequest {
    String firstName;
    String lastName;
    String city;
    LocalDate dob;
}
