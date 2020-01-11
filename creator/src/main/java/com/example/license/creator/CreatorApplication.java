package com.example.license.creator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
/**
 * @author lwl
 */
@SpringBootApplication
@PropertySource(value = {"classpath:license-creator-param.properties"})
public class CreatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreatorApplication.class, args);
    }

}
