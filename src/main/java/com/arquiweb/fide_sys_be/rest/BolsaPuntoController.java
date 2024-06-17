package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arquiweb.fide_sys_be.entity.BolsaPunto;
import com.arquiweb.fide_sys_be.service.BolsaPuntoService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/bolsa-punto")
public class BolsaPuntoController extends BaseRest<BolsaPunto>{

    private final BolsaPuntoService bolsaPuntoService;

    public BolsaPuntoController(BolsaPuntoService bolsaPuntoService) {
        this.bolsaPuntoService = bolsaPuntoService;
    }

    @Override
    public BolsaPuntoService getService() {
        return bolsaPuntoService;
    }


    @PostMapping("/save-puntos")
    public ResponseEntity<BolsaPunto> savePuntosCliente(@RequestParam(required = false)  Long clienteId ,
    @RequestParam(required = false)int montoOperacion  ) {
        
        BolsaPunto  bolsaPunto = bolsaPuntoService.guardarConPuntos(clienteId, montoOperacion);
        
        return new ResponseEntity<BolsaPunto>(bolsaPunto, HttpStatus.CREATED);
    }

    @GetMapping("/calcular-monto")
    public ResponseEntity<String> calcularMonto(@RequestParam(required = false) int monto ) {
        
        int puntos = bolsaPuntoService.obtenerPuntosPorRango(monto);
        String response = "Se obtendra :" + puntos;
         return new ResponseEntity<String>(response, HttpStatus.OK);
    }
}
