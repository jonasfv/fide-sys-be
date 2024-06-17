package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arquiweb.fide_sys_be.entity.BolsaPunto;
import com.arquiweb.fide_sys_be.entity.Cliente;
import com.arquiweb.fide_sys_be.service.ClienteService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/clientes")
public class ClienteController extends BaseRest<Cliente>{

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public ClienteService getService() {
        return clienteService;
    }

    @GetMapping("/cliente-puntos")
    public ResponseEntity<List<Cliente>> getClientesPuntosVencer(@RequestParam(required = false)  int numDias) {

        List<Cliente>  listaCliente = clienteService.getClientePuntosVencidos(numDias);


        return new ResponseEntity<List<Cliente>>(listaCliente, HttpStatus.OK);

    }
    

}
