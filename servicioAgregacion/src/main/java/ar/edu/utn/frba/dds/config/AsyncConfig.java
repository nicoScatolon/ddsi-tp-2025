package ar.edu.utn.frba.dds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncConfig {

    @Bean(name = "executorColecciones")
    public Executor executorColecciones() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean(name = "executorSolicitudes")
    public Executor executorSolicitudes() {
        return Executors.newFixedThreadPool(5);
    }
}
