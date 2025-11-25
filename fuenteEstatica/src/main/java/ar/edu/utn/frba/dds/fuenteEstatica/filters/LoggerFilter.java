package ar.edu.utn.frba.dds.fuenteEstatica.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        try {
            response.setHeader("X-Request-Id", requestId);
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;

            // Línea tipo ELF-like
            log.info(
                    "{} {} {} {} {} {} {} {}",
                    LocalDate.now(),                 // date
                    LocalTime.now(),                 // time
                    requestId,                       // request-id
                    request.getMethod(),             // method
                    request.getRequestURI(),         // uri
                    request.getQueryString(),        // query
                    response.getStatus(),            // status
                    duration                         // time-taken-ms
            );
        }
    }
}
