package com.workhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required.")
    private String currentPassword;

    @NotBlank(message = "New password is required.")
    @Pattern(
            regexp = "^[A-Za-z\\d@$!%*?&]{8,}$",
            message = "New password must be at least 8 characters long and can include letters, numbers, or special characters."
    )
    private String newPassword;

    @NotBlank(message = "Confirmation password is required.")
    @Pattern(
            regexp = "^[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Confirmation password must be at least 8 characters long and can include letters, numbers, or special characters."
    )
    private String confirmationPassword;
}
