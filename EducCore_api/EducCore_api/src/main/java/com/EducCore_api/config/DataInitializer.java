package com.EducCore_api.config;

import com.EducCore_api.entity.Role;
import com.EducCore_api.entity.User;
import com.EducCore_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByEmail("admin@authapi.com")) {
            var admin = User.builder()
                    .name("Administrador")
                    .email("admin@authapi.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("✅ Admin criado: admin@authapi.com / admin123");
        }

        if (!userRepository.existsByEmail("user@authapi.com")) {
            var user = User.builder()
                    .name("Usuário Teste")
                    .email("user@authapi.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
            log.info("✅ Usuário teste criado: user@authapi.com / user123");
        }
    }
}
