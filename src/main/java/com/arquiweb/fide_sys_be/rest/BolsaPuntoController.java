package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arquiweb.fide_sys_be.entity.BolsaPunto;
import com.arquiweb.fide_sys_be.service.BolsaPuntoService;
import com.arquiweb.fide_sys_be.service.ClienteService;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/bolsa-punto")
public class BolsaPuntoController extends BaseRest<BolsaPunto> {

    private final BolsaPuntoService bolsaPuntoService;

    public BolsaPuntoController(BolsaPuntoService bolsaPuntoService) {
        this.bolsaPuntoService = bolsaPuntoService;
    }

    @Override
    public BolsaPuntoService getService() {
        return bolsaPuntoService;
    }

    @PostMapping("/save-puntos")
    public ResponseEntity<BolsaPunto> savePuntosCliente(@RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) int montoOperacion) {

        BolsaPunto bolsaPunto = bolsaPuntoService.guardarConPuntos(clienteId, montoOperacion);

        return new ResponseEntity<BolsaPunto>(bolsaPunto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/calcular-monto", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> calcularMonto(@RequestParam(required = false) int monto) {
        int puntos = bolsaPuntoService.obtenerPuntosPorRango(monto);
        String response = "Se obtendra :" + puntos;
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<BolsaPunto>> getByClienteId(
            @PathVariable Long clienteId,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<BolsaPunto> page = bolsaPuntoService.findByClienteId(clienteId, pageable);
        if (page.hasContent()) {
            return new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/rango-puntos")
    public ResponseEntity<Page<BolsaPunto>> getByPuntajeAsignadoBetween(
            @RequestParam int minPuntaje,
            @RequestParam int maxPuntaje,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<BolsaPunto> bolsas = bolsaPuntoService.findByPuntajeAsignadoBetween(minPuntaje, maxPuntaje ,pageable);
        if (!bolsas.isEmpty()) {
            return ResponseEntity.ok(bolsas);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
