package com.arquiweb.fide_sys_be.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.UsoDetalle;
import com.arquiweb.fide_sys_be.repository.UsoDetalleRepository;

@Service
public class UsoDetalleService extends BaseService<UsoDetalle>{



    @Autowired
    UsoDetalleRepository usoDetalleRepository;

    @Override
    public UsoDetalleRepository getRepository(){
        return usoDetalleRepository;
    }

    @Override
    public Class<UsoDetalle> getClassType() {
        return UsoDetalle.class;
    }

    @Override
    public HashMap<String, String> getOperationForFindAll() {

        HashMap<String, String> operation = new HashMap<>();
        operation.put("nombre", WhereOperationConstant.FULL_LIKE);
        operation.put("apellido", WhereOperationConstant.FULL_LIKE);

        return operation;
    }
}
