package com.example.seeder;

import com.example.entity.Example;
import com.example.repository.ExampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleSeeder {

    @Autowired
    private ExampleRepository exampleRepository;

    void execute() {
        log.info("Executing ExampleSeeder");
        for (int i = 0; i < 25; i++) {
            exampleRepository.save(Example.builder()
                .value("example_value_" + i)
                .build());
        }
    }

}
