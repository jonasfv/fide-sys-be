package com.arquiweb.fide_sys_be.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.ReglaPunto;
import com.arquiweb.fide_sys_be.repository.ReglaPuntoRepository;

@Service
public class ReglaPuntoService extends BaseService<ReglaPunto>{



    @Autowired
    ReglaPuntoRepository reglaPuntoRepository;

    @Override
    public ReglaPuntoRepository getRepository(){
        return reglaPuntoRepository;
    }

    @Override
    public Class<ReglaPunto> getClassType() {
        return ReglaPunto.class;
    }

    @Override
    public HashMap<String, String> getOperationForFindAll() {

        HashMap<String, String> operation = new HashMap<>();
        operation.put("nombre", WhereOperationConstant.FULL_LIKE);
        operation.put("apellido", WhereOperationConstant.FULL_LIKE);

        return operation;
    }
}
