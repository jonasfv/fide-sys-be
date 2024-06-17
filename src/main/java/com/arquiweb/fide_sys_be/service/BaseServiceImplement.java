package com.arquiweb.fide_sys_be.service;



import java.util.List;

import com.arquiweb.fide_sys_be.entity.base.ListaPaginada;

public abstract interface BaseServiceImplement<T> {

    public List<T> findAll();
    public T getById(Long id);
    public T create(T entity);
    public void delete(Long id);
    public T update(T entity);
    public Boolean existsById(Long id);
    public abstract Class<T> getClassType();
    public ListaPaginada<T> findAllPageable(T filtros, int pageIndex, int pageSize, List<String> orderBy, List<String> orderDir);

}
