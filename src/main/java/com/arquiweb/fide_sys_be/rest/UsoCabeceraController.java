package com.arquiweb.fide_sys_be.rest;

import java.time.LocalDate;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.arquiweb.fide_sys_be.entity.UsoCabecera;
import com.arquiweb.fide_sys_be.entity.UsoDTO;
import com.arquiweb.fide_sys_be.service.UsoCabeceraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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


    @GetMapping("/cliente/{clienteId}")
    public Page<UsoCabecera> getUsoCabecerasByClienteId(
        @PathVariable Long clienteId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usoCabeceraService.getUsoCabecerasByClienteId(clienteId, page, size);
    }

    @GetMapping("/concepto/{conceptoUsoId}")
    public Page<UsoCabecera> getUsoCabecerasByConceptoUsoId(
        @PathVariable Long conceptoUsoId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usoCabeceraService.getUsoCabecerasByConceptoUsoId(conceptoUsoId, page, size);
    }

    @GetMapping("/fecha")
    public Page<UsoCabecera> getUsoCabecerasByFechaTransaccion(
        @RequestParam LocalDate fechaTransaccion,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usoCabeceraService.getUsoCabecerasByFechaTransaccion(fechaTransaccion, page, size);
    }

}
