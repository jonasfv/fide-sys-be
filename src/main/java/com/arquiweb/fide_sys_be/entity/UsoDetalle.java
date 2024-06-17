package com.arquiweb.fide_sys_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "uso_detalle")
public class UsoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private int puntajetulizado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bolsa_id")
    private BolsaPunto bolsaPuntos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cabecera_id")
    private UsoCabecera cabecera;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   
    public int getPuntajetulizado() {
        return puntajetulizado;
    }

    public void setPuntajetulizado(int puntajetulizado) {
        this.puntajetulizado = puntajetulizado;
    }

    public BolsaPunto getBolsaPuntos() {
        return bolsaPuntos;
    }

    public void setBolsaPuntos(BolsaPunto bolsaPuntos) {
        this.bolsaPuntos = bolsaPuntos;
    }

    public UsoCabecera getCabecera() {
        return cabecera;
    }

    public void setCabecera(UsoCabecera cabecera) {
        this.cabecera = cabecera;
    }


   
}
