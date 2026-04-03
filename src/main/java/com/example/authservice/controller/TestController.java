package com.example.authservice.controller;

import com.example.authservice.dto.AuthDto.MessageResponse;
import com.example.authservice.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Authorization", description = "API kiểm tra phân quyền theo role")
@SecurityRequirement(name = "Bearer Authentication")
public class TestController {

    // ==================== PUBLIC ====================
    @GetMapping("/public")
    @Operation(
        summary = "Public - Ai cũng truy cập được",
        description = "Endpoint không yêu cầu xác thực",
        security = {}
    )
    public ResponseEntity<?> publicAccess() {
        return ResponseEntity.ok(new MessageResponse("✅ Public Content - Không cần đăng nhập"));
    }

    // ==================== AUTHENTICATED USER ====================
    @GetMapping("/me")
    @Operation(
        summary = "Thông tin người dùng hiện tại",
        description = "Yêu cầu đăng nhập (bất kỳ role nào)"
    )
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("id", userDetails.getId());
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    // ==================== ROLE_USER ====================
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(
        summary = "User Content",
        description = "Yêu cầu role: USER, MODERATOR hoặc ADMIN"
    )
    public ResponseEntity<?> userAccess() {
        return ResponseEntity.ok(new MessageResponse("✅ User Content - Bạn có quyền USER"));
    }

    // ==================== ROLE_MODERATOR ====================
    @GetMapping("/moderator")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(
        summary = "Moderator Content",
        description = "Yêu cầu role: MODERATOR hoặc ADMIN"
    )
    public ResponseEntity<?> moderatorAccess() {
        return ResponseEntity.ok(new MessageResponse("✅ Moderator Content - Bạn có quyền MODERATOR"));
    }

    // ==================== ROLE_ADMIN ====================
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Admin Content",
        description = "Yêu cầu role: ADMIN"
    )
    public ResponseEntity<?> adminAccess() {
        return ResponseEntity.ok(new MessageResponse("✅ Admin Content - Bạn có quyền ADMIN"));
    }
}
