package com.medicloud.module.admin.dto;

import com.medicloud.module.user.model.Role;
import lombok.Data;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<Role> roles;
}