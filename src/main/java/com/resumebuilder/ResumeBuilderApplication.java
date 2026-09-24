package com.resumebuilder;

import com.resumebuilder.entity.ResumeTemplate;
import com.resumebuilder.entity.Role;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.ResumeTemplateRepository;
import com.resumebuilder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ResumeBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeBuilderApplication.class, args);
    }

    /**
     * Seeds a default administrator account on first startup so the
     * Admin module (template management / usage monitoring) is reachable
     * immediately without a separate registration step.
     */
    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository,
                                 @Value("${app.admin.email}") String adminEmail,
                                 @Value("${app.admin.password}") String adminPassword) {
        return args -> {
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setFullName("System Administrator");
                admin.setEmail(adminEmail);
                admin.setPassword(adminPassword); // demo-only; see PasswordUtil note in UserService
                admin.setRole(Role.ADMIN);
                admin.setCreatedAt(LocalDateTime.now());
                userRepository.save(admin);
            }
        };
    }

    @Bean
    CommandLineRunner seedTemplates(ResumeTemplateRepository templateRepository) {
        return args -> {
            if (templateRepository.count() == 0) {
                ResumeTemplate classic = new ResumeTemplate();
                classic.setTemplateName("Classic");
                classic.setDescription("Traditional single-column layout in black and grey. Suits most campus placements.");
                classic.setStyleKey("classic");
                templateRepository.save(classic);

                ResumeTemplate modern = new ResumeTemplate();
                modern.setTemplateName("Modern");
                modern.setDescription("Blue-accented layout with a bolder header. Suits design/tech roles.");
                modern.setStyleKey("modern");
                templateRepository.save(modern);
            }
        };
    }
}
