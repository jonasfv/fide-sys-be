package com.arquiweb.fide_sys_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reglas_punto")

public class ReglaPunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private int limiteSuperior;


    @Column(nullable = true)
    private int limiteInferior;
    
    @Column(nullable = false)
    private int montoPorPunto;


    public int getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(int limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public int getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(int limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public int getMontoPorPunto() {
        return montoPorPunto;
    }

    public void setMontoPorPunto(int montoPorPunto) {
        this.montoPorPunto = montoPorPunto;
    }

    
}
