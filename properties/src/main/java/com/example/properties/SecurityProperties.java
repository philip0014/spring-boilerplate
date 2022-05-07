package com.example.properties;

import com.example.enumeration.PermittedMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("app.security.config")
public class SecurityProperties {

    private String secretKey;

    private Duration timeValidity;

    @Builder.Default
    private List<PermittedEndpoint> permittedEndpoints = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PermittedEndpoint {

        @Builder.Default
        private String path = "";

        @Builder.Default
        private PermittedMethod method = PermittedMethod.GET;

    }

}
