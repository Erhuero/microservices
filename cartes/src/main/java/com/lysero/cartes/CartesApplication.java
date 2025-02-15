package com.lysero.cartes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(
        scanBasePackages = {
                "com.lysero.cartes",
                "com.lysero.amqp"
        })
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.lysero.clients"})
@PropertySources({
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
})
@EntityScan(basePackages = "com.lysero.cartes")
public class CartesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartesApplication.class, args);
    }
}
