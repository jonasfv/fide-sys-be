package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.arquiweb.fide_sys_be.entity.UsoDetalle;
import com.arquiweb.fide_sys_be.service.UsoDetalleService;

@RestController
@RequestMapping("/uso-detalle")
public class UsoDetalleController extends BaseRest<UsoDetalle>{

    private final UsoDetalleService clienteService;

    public UsoDetalleController(UsoDetalleService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public UsoDetalleService getService() {
        return clienteService;
    }


}
