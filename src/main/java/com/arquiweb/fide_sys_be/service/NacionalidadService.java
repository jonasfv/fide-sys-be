package com.arquiweb.fide_sys_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.arquiweb.fide_sys_be.entity.Nacionalidad;
import com.arquiweb.fide_sys_be.repository.NacionalidadRepository;


@Service
public class NacionalidadService extends BaseService<Nacionalidad> {

    @Autowired
    NacionalidadRepository nacionalidadRepository;

    @Override
    public NacionalidadRepository getRepository(){
        return nacionalidadRepository;
    }

    @Override
    public Class<Nacionalidad> getClassType() {
        return Nacionalidad.class;
    }


}
