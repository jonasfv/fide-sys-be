package com.arquiweb.fide_sys_be.entity;

import java.time.LocalDate;

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
@Table(name = "uso")
public class UsoCabecera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private int puntosUtilizados;

    @Column(nullable = true)
    private LocalDate fechaTransaccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "concepto_id")
    private ConceptoUso conceptoUso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntosUtilizados() {
        return puntosUtilizados;
    }

    public void setPuntosUtilizados(int puntosUtilizados) {
        this.puntosUtilizados = puntosUtilizados;
    }

    public LocalDate getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDate fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ConceptoUso getConceptoUso() {
        return conceptoUso;
    }

    public void setConceptoUso(ConceptoUso conceptoUso) {
        this.conceptoUso = conceptoUso;
    }


    
}
