package com.example.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("app.config")
public class ApplicationProperties {

    @Builder.Default
    private Database database = new Database();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Database {

        @Builder.Default
        private boolean seederActive = false;

    }

}
