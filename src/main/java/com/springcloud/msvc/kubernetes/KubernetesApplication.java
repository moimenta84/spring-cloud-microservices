package com.springcloud.msvc.kubernetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Enables Feign clients in the application. Feign is a declarative HTTP client that allows
// communication with other microservices by defining annotated interfaces, without writing manual HTTP call code.
@EnableFeingClients
@SpringBootApplication
public class KubernetesApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubernetesApplication.class, args);
    }

}
