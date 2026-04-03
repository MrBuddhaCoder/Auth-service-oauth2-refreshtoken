# 🔐 Auth Service - Spring Boot + OAuth2 + JWT

## Tổng quan

Auth Service cung cấp xác thực và phân quyền hoàn chỉnh sử dụng:

- **Spring Boot 3.2** + Spring Security 6
- **OAuth2 Resource Server** - bảo vệ API endpoints
- **JWT (JSON Web Token)** - access token có thời hạn
- **Refresh Token** - lưu trong database, hỗ trợ rotation
- **RBAC** - phân quyền theo role (USER, MODERATOR, ADMIN)
- **Swagger UI** - giao diện test API trực quan với hỗ trợ JWT

---

## Cấu trúc Project

```
auth-service/
├── src/main/java/com/example/authservice/
│   ├── config/
│   │   ├── DataInitializer.java       # Seed roles + default users
│   │   ├── GlobalExceptionHandler.java # Xử lý exception toàn cục
│   │   ├── OpenApiConfig.java          # Cấu hình Swagger + JWT auth
│   │   └── SecurityConfig.java         # Spring Security + OAuth2
│   ├── controller/
│   │   ├── AuthController.java         # API: signin, signup, refresh, logout
│   │   └── TestController.java         # API test phân quyền theo role
│   ├── dto/
│   │   └── AuthDto.java                # Request/Response DTOs
│   ├── entity/
│   │   ├── ERole.java                  # Enum roles
│   │   ├── RefreshToken.java           # Refresh token entity
│   │   ├── Role.java                   # Role entity
│   │   └── User.java                   # User entity
│   ├── repository/
│   │   ├── RefreshTokenRepository.java
│   │   ├── RoleRepository.java
│   │   └── UserRepository.java
│   ├── security/
│   │   ├── AuthEntryPointJwt.java      # Xử lý 401 Unauthorized
│   │   ├── JwtAuthenticationFilter.java # Filter xác thực JWT
│   │   ├── JwtUtils.java               # Tạo/validate JWT
│   │   ├── UserDetailsImpl.java        # UserDetails implementation
│   │   └── UserDetailsServiceImpl.java # Load user từ DB
│   ├── service/
│   │   └── RefreshTokenService.java    # CRUD refresh token
│   └── AuthServiceApplication.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

---

## Yêu cầu

- Java 17+
- Maven 3.8+

---

## Chạy ứng dụng

```bash
cd auth-service
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

---

## Tài khoản mặc định

| Username    | Password   | Roles              |
|-------------|------------|--------------------|
| `admin`     | `admin123` | ADMIN + USER       |
| `moderator` | `mod123`   | MODERATOR + USER   |
| `user`      | `user123`  | USER               |

---

## Swagger UI

Truy cập: **http://localhost:8080/swagger-ui.html**

### Cách test trên Swagger:

1. Gọi `POST /api/auth/signin` với username/password
2. Copy `accessToken` từ response
3. Click nút **Authorize** 🔒 (góc trên bên phải)
4. Nhập token → Click **Authorize**
5. Gọi các API protected để test phân quyền

---

## API Endpoints

### 🔓 Authentication (Public)

| Method | Endpoint                  | Mô tả                        |
|--------|---------------------------|-------------------------------|
| POST   | `/api/auth/signup`        | Đăng ký tài khoản mới        |
| POST   | `/api/auth/signin`        | Đăng nhập → JWT + Refresh    |
| POST   | `/api/auth/refresh-token` | Làm mới access token          |
| POST   | `/api/auth/logout`        | Đăng xuất, hủy refresh token |

### 🔒 Test Authorization (Protected)

| Method | Endpoint             | Quyền truy cập                |
|--------|----------------------|-------------------------------|
| GET    | `/api/test/public`   | Tất cả (không cần đăng nhập)  |
| GET    | `/api/test/me`       | Authenticated (bất kỳ role)   |
| GET    | `/api/test/user`     | USER, MODERATOR, ADMIN        |
| GET    | `/api/test/moderator`| MODERATOR, ADMIN              |
| GET    | `/api/test/admin`    | ADMIN                         |

---

## Luồng xác thực

```
1. Client gửi POST /api/auth/signin {username, password}
                    │
2. Server xác thực → Tạo JWT access token (15 phút)
                   → Tạo refresh token (7 ngày, lưu DB)
                    │
3. Client nhận {accessToken, refreshToken}
                    │
4. Client gọi API với Header: Authorization: Bearer <accessToken>
                    │
5. JwtAuthenticationFilter validate token → cho phép/từ chối
                    │
6. Khi accessToken hết hạn:
   Client gửi POST /api/auth/refresh-token {refreshToken}
   → Nhận accessToken mới + refreshToken mới (rotation)
```

---

## Cấu hình JWT

Chỉnh sửa trong `application.properties`:

```properties
# Secret key (Base64 encoded, tối thiểu 256-bit)
app.jwt.secret=your-base64-secret-key

# Access token: 15 phút (900000ms)
app.jwt.access-token-expiration-ms=900000

# Refresh token: 7 ngày (604800000ms)
app.jwt.refresh-token-expiration-ms=604800000
```

---

## Chuyển sang Production

### 1. Đổi database sang MySQL/PostgreSQL:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/authdb
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=secret
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### 2. Thay đổi JWT secret key:

```bash
# Tạo secret key mạnh
openssl rand -base64 64
```

### 3. Tắt H2 Console và Swagger (production):

```properties
spring.h2.console.enabled=false
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```
