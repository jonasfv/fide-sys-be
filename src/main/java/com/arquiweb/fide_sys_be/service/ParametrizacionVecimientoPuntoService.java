package com.arquiweb.fide_sys_be.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.ParametrizacionVecimientoPunto;
import com.arquiweb.fide_sys_be.repository.ParametrizacionVecimientoPuntoRepository;

@Service
public class ParametrizacionVecimientoPuntoService extends BaseService<ParametrizacionVecimientoPunto>{



    @Autowired
    ParametrizacionVecimientoPuntoRepository parametrizacionVecimientoPuntoRepository;

    @Override
    public ParametrizacionVecimientoPuntoRepository getRepository(){
        return parametrizacionVecimientoPuntoRepository;
    }

    @Override
    public Class<ParametrizacionVecimientoPunto> getClassType() {
        return ParametrizacionVecimientoPunto.class;
    }

    @Override
    public HashMap<String, String> getOperationForFindAll() {

        HashMap<String, String> operation = new HashMap<>();
        operation.put("nombre", WhereOperationConstant.FULL_LIKE);
        operation.put("apellido", WhereOperationConstant.FULL_LIKE);

        return operation;
    }
}
