package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.properties")
})
@ComponentScans(
        @ComponentScan("org.example")
)
public class Main {
    public static void main(String[] args)  {

        SpringApplication.run(Main.class, args);
    }
}