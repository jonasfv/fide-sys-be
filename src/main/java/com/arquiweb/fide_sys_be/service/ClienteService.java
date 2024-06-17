package com.arquiweb.fide_sys_be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.BolsaPunto;
import com.arquiweb.fide_sys_be.entity.Cliente;
import com.arquiweb.fide_sys_be.repository.BolsaPuntoRepository;
import com.arquiweb.fide_sys_be.repository.ClienteRepository;

@Service
public class ClienteService extends BaseService<Cliente>{



    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    BolsaPuntoRepository bolsaPuntoRepository;

    @Override
    public ClienteRepository getRepository(){
        return clienteRepository;
    }

    @Override
    public Class<Cliente> getClassType() {
        return Cliente.class;
    }

    @Override
    public HashMap<String, String> getOperationForFindAll() {

        HashMap<String, String> operation = new HashMap<>();
        operation.put("nombre", WhereOperationConstant.FULL_LIKE);
        operation.put("apellido", WhereOperationConstant.FULL_LIKE);

        return operation;
    }

    public List<Cliente> getClientePuntosVencidos(int dias){

        LocalDate fecha = LocalDate.now().minusDays(dias);

        List<BolsaPunto>  listaBolsa = bolsaPuntoRepository.findByFechaCaducidad(fecha);
        List<Cliente> listaCliente= new ArrayList<>();

        for (BolsaPunto bolsa : listaBolsa) {

            listaCliente.add(bolsa.getCliente());

        }
            return listaCliente;

    }
}
