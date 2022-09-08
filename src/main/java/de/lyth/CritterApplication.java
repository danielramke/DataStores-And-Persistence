package de.lyth;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * This class provides the main spring boot content.
 * The class is absolute needed to run the application.
 * @author Daniel Ramke
 * @see SpringApplication
 */
@SpringBootApplication
public class CritterApplication {

    /**
     * This launched the default SpringBoot application {@link SpringApplication#run(String...)} method.
     * @param args the given program arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(CritterApplication.class, args);
    }

    /**
     * creates an instance of ModelMapper to perform model thinks.
     * @return the created ModelMapper.
     */
    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }

}
