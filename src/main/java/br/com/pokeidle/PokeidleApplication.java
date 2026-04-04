package br.com.pokeidle;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PokeidleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokeidleApplication.class, args);
    }

}
