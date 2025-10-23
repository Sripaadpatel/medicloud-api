package com.medicloud.module.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotEmpty @Email
    private String email;

    @NotEmpty @Size(min = 6, max = 20)
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    // By default, a new registration is always a patient.
    // An admin can add other roles later.
}