package com.workhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Pattern(
            regexp = "^[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and can include letters, numbers, or special characters."
    )
    String password;
}
