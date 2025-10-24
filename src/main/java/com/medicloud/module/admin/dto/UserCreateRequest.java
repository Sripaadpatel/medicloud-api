package com.medicloud.module.admin.dto;

import com.medicloud.module.user.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;

@Data
public class UserCreateRequest {
    @NotEmpty @Email
    private String email;

    @NotEmpty @Size(min = 6, max = 20)
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String phoneNumber;

    @NotEmpty // Must assign at least one role
    private Set<Role> roles; // Admin specifies the roles
}