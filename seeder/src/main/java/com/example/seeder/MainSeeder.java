package com.example.seeder;

import com.example.properties.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class MainSeeder {

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private List<BaseSeeder> seeders;

    @PostConstruct
    public void init() {
        boolean isDevProfile = false;
        for (String profile : environment.getActiveProfiles()) {
            if ("dev".equals(profile)) {
                isDevProfile = true;
                break;
            }
        }

        if (isDevProfile && applicationProperties.getDatabase().isSeederActive()) {
            for (BaseSeeder seeder : seeders) {
                log.info("Executing seeder {}", seeder.getClass().getSimpleName());
                seeder.execute();
            }
        }
    }

}
