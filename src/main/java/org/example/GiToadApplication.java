package org.example;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import java.io.IOException;

@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:application.properties")
})
@ComponentScans(
        @ComponentScan("org.example")
)
public class GiToadApplication {
    public static void main(String[] args) throws IOException {

        SpringApplication.run(GiToadApplication.class, args);

    }
}