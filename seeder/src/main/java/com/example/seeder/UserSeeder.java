package com.example.seeder;

import com.example.entity.User;
import com.example.enumeration.UserRole;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements BaseSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void execute() {
        User adminUser = User.builder()
            .username("admin-user")
            .password(passwordEncoder.encode("admin-password"))
            .userRole(UserRole.ADMIN)
            .build();
        userRepository.save(adminUser);

        User basicUser = User.builder()
            .username("basic-user")
            .password(passwordEncoder.encode("basic-password"))
            .userRole(UserRole.BASIC)
            .build();
        userRepository.save(basicUser);
    }

}
