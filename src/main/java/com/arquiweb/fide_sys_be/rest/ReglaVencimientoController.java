package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.arquiweb.fide_sys_be.entity.ParametrizacionVecimientoPunto;
import com.arquiweb.fide_sys_be.service.ParametrizacionVecimientoPuntoService;



@RestController
@RequestMapping("/regla-vencimiento")
public class ReglaVencimientoController extends BaseRest<ParametrizacionVecimientoPunto>{

    private final ParametrizacionVecimientoPuntoService paramService;
 
    public ReglaVencimientoController(ParametrizacionVecimientoPuntoService paramService) {
        this.paramService = paramService;
    }

    @Override
    public ParametrizacionVecimientoPuntoService getService() {
        return paramService;
    }
    
}
