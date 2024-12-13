package com.workhub.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(max = 100)
    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotNull(message = "Role is required.")
    private Role role;

    @NotBlank(message = "Password is required.")
    @Pattern(
            regexp = "^[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and can include letters, numbers, or special characters."
    )
    private String password;
}
