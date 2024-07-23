package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.arquiweb.fide_sys_be.entity.Nacionalidad;
import com.arquiweb.fide_sys_be.service.NacionalidadService;

@RestController
@RequestMapping("/nacionalidad")
public class NacionalidadController  extends BaseRest<Nacionalidad>{
 

    private final NacionalidadService nacionalidadService;

    public NacionalidadController(NacionalidadService nacionalidadService) {
        this.nacionalidadService = nacionalidadService;
    }

    @Override
    public NacionalidadService getService() {
        return nacionalidadService;
    }

    
}
