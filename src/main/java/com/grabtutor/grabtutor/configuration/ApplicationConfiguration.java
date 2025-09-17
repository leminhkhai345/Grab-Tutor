package com.grabtutor.grabtutor.configuration;
import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfiguration {
    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_MAIL = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("Initializing application.....");
        return args -> {
            if(!userRepository.findByEmail(ADMIN_MAIL).isPresent()) {
                User user = User.builder()
                        .email(ADMIN_MAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
                log.info("Application initialization completed .....");
            }
        };
    }
}
