package com.arquiweb.fide_sys_be.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arquiweb.fide_sys_be.entity.BolsaPunto;

@Repository
public interface BolsaPuntoRepository extends JpaRepository<BolsaPunto, Long>, JpaSpecificationExecutor<BolsaPunto> {

    @Query("SELECT b FROM BolsaPunto b WHERE b.cliente.id = ?1 and b.saldo > 0  ORDER BY b.fechaAsignacion ASC")
    List<BolsaPunto> findByClienteId(Long clienteId);


    @Query("SELECT bp FROM BolsaPunto bp WHERE bp.fechaCaducidad = :fecha")
     List<BolsaPunto> findByFechaCaducidad(@Param("fecha") LocalDate fecha);

}
