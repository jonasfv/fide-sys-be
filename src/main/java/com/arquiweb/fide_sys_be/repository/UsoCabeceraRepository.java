package com.arquiweb.fide_sys_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.arquiweb.fide_sys_be.entity.UsoCabecera;

@Repository
public interface UsoCabeceraRepository  extends JpaRepository<UsoCabecera, Long>  , JpaSpecificationExecutor<UsoCabecera> {

}
