package com.example.seeder;

import com.example.entity.Example;
import com.example.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleSeeder implements BaseSeeder {

    @Autowired
    private ExampleRepository exampleRepository;

    @Override
    public void execute() {
        for (int i = 0; i < 25; i++) {
            exampleRepository.save(
                Example.builder()
                    .value("example_value_" + i)
                    .build()
            );
        }
    }

}
