package com.arquiweb.fide_sys_be.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SchedulerConfig {
    
    @Value("${cron.cambio.puntos.vencios}")
    private String cronCambioEstadoString;

    public String getCronCambioEstado() {
        return cronCambioEstadoString;
    }
}
