package com.arquiweb.fide_sys_be.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arquiweb.fide_sys_be.entity.ConceptoUso;
import com.arquiweb.fide_sys_be.service.ConceptoUsoService;

@RestController
@RequestMapping("/concepto-uso")
public class ConceptoUsoController extends BaseRest<ConceptoUso>{

    private final ConceptoUsoService conceptoUsoService;

    public ConceptoUsoController(ConceptoUsoService conceptoUsoService) {
        this.conceptoUsoService = conceptoUsoService;
    }

    @Override
    public ConceptoUsoService getService() {
        return conceptoUsoService;
    }


}
