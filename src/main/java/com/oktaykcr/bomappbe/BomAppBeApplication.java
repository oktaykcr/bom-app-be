package com.oktaykcr.bomappbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BomAppBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BomAppBeApplication.class, args);
    }

}
