package com.example.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("app.security.config")
public class SecurityProperties {

    private String secretKey;

    private Duration timeValidity;

}
