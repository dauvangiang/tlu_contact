package com.mobile.group.tlu_contact_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TluContactBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TluContactBeApplication.class, args);
    }

}
