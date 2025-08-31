package ar.edu.utn.frba.dds.fuenteDinamica.models.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean(name = "executorHechos")
    public Executor executorHechos() {
        return Executors.newFixedThreadPool(5); // ajustá la cantidad de threads
    }
}
