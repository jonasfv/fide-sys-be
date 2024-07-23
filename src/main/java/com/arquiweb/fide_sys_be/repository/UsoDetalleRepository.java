package com.arquiweb.fide_sys_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.arquiweb.fide_sys_be.entity.UsoDetalle;
import java.util.List;


@Repository
public interface UsoDetalleRepository  extends JpaRepository<UsoDetalle, Long>  , JpaSpecificationExecutor<UsoDetalle> {

    List<UsoDetalle> findByCabecera_Id(Long id);
}
