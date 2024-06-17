package com.arquiweb.fide_sys_be.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arquiweb.fide_sys_be.entity.BolsaPunto;
import com.arquiweb.fide_sys_be.entity.UsoCabecera;
import com.arquiweb.fide_sys_be.entity.UsoDTO;
import com.arquiweb.fide_sys_be.service.UsoCabeceraService;

@RestController
@RequestMapping("/uso-cabecera")
public class UsoCabeceraController extends BaseRest<UsoCabecera>{

    private final UsoCabeceraService usoCabeceraService;

    public UsoCabeceraController(UsoCabeceraService usoCabeceraService) {
        this.usoCabeceraService = usoCabeceraService;
    }

    @Override
    public UsoCabeceraService getService() {
        return usoCabeceraService;
    }


    @PostMapping("/save-uso")
    public ResponseEntity<UsoDTO> savePuntosCliente(@RequestParam(required = false)  Long clienteId ,
    @RequestParam(required = false) Long conceptoId  ) {
        
        UsoDTO  uso = usoCabeceraService.generarUso(clienteId, conceptoId);
        
        return new ResponseEntity<UsoDTO>(uso, HttpStatus.CREATED);
    }


}
