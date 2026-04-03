package com.example.authservice.config;

import com.example.authservice.entity.ERole;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Tạo roles
        createRoleIfNotFound(ERole.ROLE_USER);
        createRoleIfNotFound(ERole.ROLE_MODERATOR);
        createRoleIfNotFound(ERole.ROLE_ADMIN);

        // Tạo tài khoản admin mặc định
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@example.com",
                    passwordEncoder.encode("admin123"));

            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
            admin.setRoles(Set.of(adminRole, userRole));

            userRepository.save(admin);
            logger.info("✅ Tạo tài khoản admin mặc định: admin / admin123");
        }

        // Tạo tài khoản user mặc định
        if (!userRepository.existsByUsername("user")) {
            User user = new User("user", "user@example.com",
                    passwordEncoder.encode("user123"));

            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
            user.setRoles(Set.of(userRole));

            userRepository.save(user);
            logger.info("✅ Tạo tài khoản user mặc định: user / user123");
        }

        // Tạo tài khoản moderator mặc định
        if (!userRepository.existsByUsername("moderator")) {
            User mod = new User("moderator", "mod@example.com",
                    passwordEncoder.encode("mod123"));

            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
            mod.setRoles(Set.of(modRole, userRole));

            userRepository.save(mod);
            logger.info("✅ Tạo tài khoản moderator mặc định: moderator / mod123");
        }

        logger.info("========================================");
        logger.info("  AUTH SERVICE STARTED SUCCESSFULLY");
        logger.info("  Swagger UI: http://localhost:8080/swagger-ui.html");
        logger.info("  H2 Console: http://localhost:8080/h2-console");
        logger.info("========================================");
        logger.info("  Default accounts:");
        logger.info("  admin    / admin123  (ADMIN + USER)");
        logger.info("  moderator/ mod123    (MODERATOR + USER)");
        logger.info("  user     / user123   (USER)");
        logger.info("========================================");
    }

    private void createRoleIfNotFound(ERole roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(new Role(roleName));
            logger.info("Tạo role: {}", roleName);
        }
    }
}
