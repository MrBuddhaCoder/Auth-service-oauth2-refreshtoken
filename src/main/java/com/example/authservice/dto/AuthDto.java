package com.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

public class AuthDto {

    // ===== Requests =====

    @Data
    @Schema(description = "Yêu cầu đăng nhập")
    public static class LoginRequest {
        @NotBlank
        @Schema(description = "Tên đăng nhập", example = "admin")
        private String username;

        @NotBlank
        @Schema(description = "Mật khẩu", example = "admin123")
        private String password;
    }

    @Data
    @Schema(description = "Yêu cầu đăng ký")
    public static class SignupRequest {
        @NotBlank
        @Size(min = 3, max = 50)
        @Schema(description = "Tên đăng nhập", example = "newuser")
        private String username;

        @NotBlank
        @Size(max = 100)
        @Email
        @Schema(description = "Email", example = "newuser@example.com")
        private String email;

        @NotBlank
        @Size(min = 6, max = 120)
        @Schema(description = "Mật khẩu", example = "password123")
        private String password;

        @Schema(description = "Danh sách role (ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN)", example = "[\"ROLE_USER\"]")
        private Set<String> roles;
    }

    @Data
    @Schema(description = "Yêu cầu refresh token")
    public static class TokenRefreshRequest {
        @NotBlank
        @Schema(description = "Refresh token")
        private String refreshToken;
    }

    @Data
    @Schema(description = "Yêu cầu đăng xuất")
    public static class LogoutRequest {
        @NotBlank
        @Schema(description = "Refresh token cần hủy")
        private String refreshToken;
    }

    // ===== Responses =====

    @Data
    @Schema(description = "Phản hồi JWT sau đăng nhập")
    public static class JwtResponse {
        @Schema(description = "Access token (JWT)")
        private String accessToken;

        @Schema(description = "Refresh token")
        private String refreshToken;

        @Schema(description = "Loại token", example = "Bearer")
        private String tokenType = "Bearer";

        @Schema(description = "ID người dùng")
        private Long id;

        @Schema(description = "Tên đăng nhập")
        private String username;

        @Schema(description = "Email")
        private String email;

        @Schema(description = "Danh sách quyền")
        private Set<String> roles;

        public JwtResponse(String accessToken, String refreshToken,
                           Long id, String username, String email, Set<String> roles) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.id = id;
            this.username = username;
            this.email = email;
            this.roles = roles;
        }
    }

    @Data
    @Schema(description = "Phản hồi refresh token")
    public static class TokenRefreshResponse {
        @Schema(description = "Access token mới")
        private String accessToken;

        @Schema(description = "Refresh token")
        private String refreshToken;

        @Schema(description = "Loại token", example = "Bearer")
        private String tokenType = "Bearer";

        public TokenRefreshResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Data
    @Schema(description = "Phản hồi thông báo chung")
    public static class MessageResponse {
        @Schema(description = "Nội dung thông báo")
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }
}
