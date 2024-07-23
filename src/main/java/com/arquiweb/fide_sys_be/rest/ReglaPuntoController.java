package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.arquiweb.fide_sys_be.entity.ReglaPunto;
import com.arquiweb.fide_sys_be.service.ReglaPuntoService;

@RestController
@RequestMapping("/regla-punto")
public class ReglaPuntoController extends BaseRest<ReglaPunto>{

    private final ReglaPuntoService reglaPuntoService;
 
    public ReglaPuntoController(ReglaPuntoService reglaPuntoService) {
        this.reglaPuntoService = reglaPuntoService;
    }

    @Override
    public ReglaPuntoService getService() {
        return reglaPuntoService;
    }


}
