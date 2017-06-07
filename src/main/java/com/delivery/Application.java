package com.delivery;


import com.delivery.config.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = WebMvcConfig.class)
@ComponentScan("com")
public class Application {
    //lx5548
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
