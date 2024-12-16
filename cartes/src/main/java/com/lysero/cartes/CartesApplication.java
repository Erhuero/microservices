package com.lysero.cartes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.lysero.cartes")
public class CartesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartesApplication.class, args);
    }

}
