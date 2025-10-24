package com.medicloud.module.admin;

import com.medicloud.module.admin.dto.UserCreateRequest;
import com.medicloud.module.admin.dto.UserResponseDTO;
import com.medicloud.module.user.model.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/users") // Base path for user management
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // ONLY Admins can access these endpoints
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * Creates a new user (Patient, Doctor, Staff, Admin)
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserResponseDTO newUser = adminUserService.createUser(userCreateRequest);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Gets a list of all users.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Gets a single user by ID.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO user = adminUserService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Updates the roles of an existing user.
     */
    @PutMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDTO> updateUserRoles(@PathVariable Long userId, @RequestBody Set<Role> roles) {
        UserResponseDTO updatedUser = adminUserService.updateUserRoles(userId, roles);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes a user by ID.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}