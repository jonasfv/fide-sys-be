package com.arquiweb.fide_sys_be.rest;

import com.arquiweb.fide_sys_be.entity.base.ListaPaginada;
import com.arquiweb.fide_sys_be.service.BaseServiceImplement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseRest<T> {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    public abstract BaseServiceImplement<T> getService();

    @GetMapping
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<Object>(getService().findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        T entity = null;
        try {
            entity = getService().getById(id);
        }
        catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No se encontró la entidad con el id: " + id);
        }
        catch (Exception e) {
            LOGGER.error("Error al obtener la entidad " +  entity.getClass().getName() + " con id: " + id + "Error: " + e.getMessage());
        }
        return new ResponseEntity<T>(entity, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid T entity, BindingResult result) {
        LOGGER.info("Guardando registro: " + entity.getClass().getName());
        LOGGER.info("Guardando registro: " + entity.toString());
        T newEntity = null;
        if(result.hasErrors()) {
            return new ResponseEntity<Object>(errors(result), HttpStatus.BAD_REQUEST);
        }
        try {
            newEntity =  getService().create(entity);
            LOGGER.info("Registro guardado: " + entity.toString());
        }
        catch (ConstraintViolationException c) {
            LOGGER.error("Error al guardar la entidad " +  entity.getClass().getName());
            return new ResponseEntity<String>(c.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            LOGGER.error("Error al guardar la entidad " +  entity.getClass().getName() + "Error: " + e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<T>(newEntity, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid T entity,BindingResult result) {
        LOGGER.info("Actualizando registro: " + entity.getClass().getName());
        LOGGER.info("Actualizando registro: " + entity.toString());
        T updateEntity = null;
        if(result.hasErrors()) {
            return new ResponseEntity<Object>(errors(result), HttpStatus.BAD_REQUEST);
        }
        try {
            updateEntity =  getService().update(entity);
            LOGGER.info("Registro actualizado: " + entity.toString());
        } catch (Exception e) {
            LOGGER.error("Error al actualizar la entidad " +  entity.getClass().getName() + "Error: " + e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<T>(updateEntity, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        LOGGER.info("Buscando entidad con id: " + id);
        T entity = null;
        try {
            if (!getService().existsById(id)) {
                LOGGER.error("No se encontro entidad con id: " + id);
                return new ResponseEntity<String>("No existe Id : "+ id, HttpStatus.NOT_FOUND);
            }else {
                entity = getService().getById(id);
                getService().delete(id);
            }
        } catch (Exception e) {
            LOGGER.error("Error al borrar la entidad con id : " + id + "Error: " + e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<T>(entity, HttpStatus.OK);
    }

    @GetMapping("/paginado")
    public ListaPaginada<T> list(@RequestParam(defaultValue = "") String filtros,
                                 @RequestParam(defaultValue = "10") final int cantidad,
                                 @RequestParam(defaultValue = "1") final int pagina,
                                 @RequestParam(defaultValue = "") final String orderBy,
                                 @RequestParam(defaultValue = "") final String orderDir

    ) throws JsonMappingException, JsonProcessingException{

        T filtrosParam = null;

        if (filtros != null && !filtros.isEmpty()) {
            //ObjectMapper mapper = new ObjectMapper()
            //        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            filtrosParam = mapper.readValue(filtros, getService().getClassType());
        }

        List<String> orderByList = new ArrayList<>();
        List<String> orderDirList = new ArrayList<>();

        if (!orderBy.isEmpty() && !orderDir.isEmpty()) {
            orderByList = Arrays.asList(orderBy.split(";"));
            orderDirList = Arrays.asList(orderDir.split(";"));
        }

        if (orderByList.size() != orderDirList.size()) {
            throw new IllegalArgumentException("Los parameters para el ordenamiento no coinciden. orderBy: " + orderBy + " orderDir: " + orderDir);
        }

        return getService().findAllPageable(filtrosParam, pagina, cantidad, orderByList, orderDirList);

    }

    public List<String> errors(BindingResult result){

        return result.getFieldErrors()
                .stream()
                .map(err -> {
                    return "El campo " + err.getField() + " " + err.getDefaultMessage();
                })
                .collect(Collectors.toList());
    }
}
