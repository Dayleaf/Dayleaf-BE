package org.example.dayleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DayleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayleafApplication.class, args);
    }

}
