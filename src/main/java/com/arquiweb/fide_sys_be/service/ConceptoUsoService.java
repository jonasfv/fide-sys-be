package com.arquiweb.fide_sys_be.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.ConceptoUso;
import com.arquiweb.fide_sys_be.repository.ConceptoUsoRepository;

@Service
public class ConceptoUsoService extends BaseService<ConceptoUso>{



    @Autowired
    ConceptoUsoRepository conceptoUsoRepository;

    @Override
    public ConceptoUsoRepository getRepository(){
        return conceptoUsoRepository;
    }

    @Override
    public Class<ConceptoUso> getClassType() {
        return ConceptoUso.class;
    }

    @Override
    public HashMap<String, String> getOperationForFindAll() {

        HashMap<String, String> operation = new HashMap<>();
        operation.put("nombre", WhereOperationConstant.FULL_LIKE);
        operation.put("apellido", WhereOperationConstant.FULL_LIKE);

        return operation;
    }
}
