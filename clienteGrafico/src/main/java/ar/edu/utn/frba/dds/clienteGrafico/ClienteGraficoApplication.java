package ar.edu.utn.frba.dds.clienteGrafico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@EnableCaching
public class ClienteGraficoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClienteGraficoApplication.class, args);
    }
}
