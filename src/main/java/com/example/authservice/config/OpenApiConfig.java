package com.example.authservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service API")
                        .version("1.0.0")
                        .description("""
                            ## Auth Service - Xác thực & Phân quyền
                            
                            Service cung cấp:
                            - **Đăng ký** tài khoản mới với phân quyền (USER, MODERATOR, ADMIN)
                            - **Đăng nhập** và nhận JWT access token + refresh token
                            - **Refresh token** để lấy access token mới khi hết hạn
                            - **Đăng xuất** và hủy refresh token
                            - **Phân quyền** dựa trên role (RBAC)
                            
                            ### Cách sử dụng:
                            1. Đăng ký tài khoản qua `/api/auth/signup`
                            2. Đăng nhập qua `/api/auth/signin` → nhận `accessToken` + `refreshToken`
                            3. Click nút **Authorize** 🔒 phía trên, nhập `accessToken`
                            4. Gọi các API được bảo vệ
                            5. Khi token hết hạn, dùng `/api/auth/refresh-token` để lấy token mới
                            """)
                        .contact(new Contact()
                                .name("Auth Service Team")
                                .email("dev@example.com"))
                        .license(new License()
                                .name("MIT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập JWT access token (không cần prefix 'Bearer')")));
    }
}
