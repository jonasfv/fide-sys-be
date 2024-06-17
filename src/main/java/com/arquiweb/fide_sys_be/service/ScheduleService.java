package com.arquiweb.fide_sys_be.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arquiweb.fide_sys_be.entity.BolsaPunto;

@Component
public class ScheduleService {

    @Autowired
    BolsaPuntoService bolsaPuntoService;



    @Scheduled(cron = "#{schedulerConfig.getCronCambioEstado()}")
    public void puntosVenciosActualizacion()  throws InterruptedException  {
        List<BolsaPunto> listPresupuestos = bolsaPuntoService.findBolsaCaducada();

        for (BolsaPunto bolsaPunto : listPresupuestos) {
            bolsaPunto.setPuntajeUtilizado(bolsaPunto.getPuntajeAsignado());
            bolsaPunto.setSaldo(0);
            bolsaPuntoService.update(bolsaPunto);

            Thread.sleep(1000); 
        }
    }
}
