package com.arquiweb.fide_sys_be.entity;

import java.util.List;

public class UsoDTO {

    private UsoCabecera cabecera;

    List<UsoDetalle> detalles;

    public UsoCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(UsoCabecera cabecera) {
        this.cabecera = cabecera;
    }

    public List<UsoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<UsoDetalle> detalles) {
        this.detalles = detalles;
    }

    
}
