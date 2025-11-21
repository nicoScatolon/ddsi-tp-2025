package ar.edu.utn.frba.dds.clienteGrafico.config;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
@Configuration
public class ErrorConfig {

    @Bean
    public ErrorViewResolver customErrorViewResolver() {
        return (request, status, model) -> {
            if (status == HttpStatus.NOT_FOUND) {
                return new ModelAndView("redirect:/error/404");
            } else if (status == HttpStatus.FORBIDDEN) {
                return new ModelAndView("redirect:/error/403");
            } else if (status == HttpStatus.BAD_REQUEST) {
                return new ModelAndView("redirect:/error/400");
            } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                return new ModelAndView("redirect:/error/500");
            }
            return null;
        };
    }
}