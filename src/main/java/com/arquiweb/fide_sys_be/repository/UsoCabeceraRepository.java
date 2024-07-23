package com.arquiweb.fide_sys_be.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.arquiweb.fide_sys_be.entity.UsoCabecera;

@Repository
public interface UsoCabeceraRepository extends JpaRepository<UsoCabecera, Long>, JpaSpecificationExecutor<UsoCabecera> {

    Page<UsoCabecera> findByClienteId(Long clienteId, Pageable pageable);

    Page<UsoCabecera> findByConceptoUsoId(Long conceptoUsoId, Pageable pageable);

    Page<UsoCabecera> findByFechaTransaccion(LocalDate fechaTransaccion, Pageable pageable);

}
