package com.ny.safeny.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication Request DTOs
 */
public class AuthDto {
    
    /**
     * Login Request DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;
        
        @NotBlank(message = "Password is required")
        private String password;
    }
    
    /**
     * Registration Request DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegisterRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
        private String username;
        
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", 
                 message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
        private String password;
        
        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        private String fullName;
        
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
        
        @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone must be in format: 518-555-0100")
        private String phone;
    }
    
    /**
     * Authentication Response DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthResponse {
        private String token;
        
        @Builder.Default
        private String type = "Bearer";
        
        private String username;
        private String fullName;
        private String email;
        private String role;
        private Long expiresIn; // milliseconds
    }
    
    /**
     * Status Update Request DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {
        @NotBlank(message = "Status is required")
        private String status;
        
        private String reviewComments;
        private java.math.BigDecimal approvedAmount;
    }
}