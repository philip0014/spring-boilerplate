package com.example.application;

import com.example.properties.ApplicationProperties;
import com.example.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.example.entity")
@SpringBootApplication(scanBasePackages = "com.example")
@EnableConfigurationProperties({ApplicationProperties.class, SecurityProperties.class})
@EnableJpaRepositories(basePackages = "com.example.repository")
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}
