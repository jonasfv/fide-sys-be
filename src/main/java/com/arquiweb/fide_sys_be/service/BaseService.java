package com.arquiweb.fide_sys_be.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.base.ListaPaginada;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class BaseService<T> implements BaseServiceImplement<T> {


   

    public abstract JpaRepository<T,Long> getRepository();


    static final String BIG_DECIMAL_CLASS = "class java.math.BigDecimal";
    static final String DATE_CLASS = "class java.util.Date";
    static final String DATE_CLASS_SQL = "class java.sql.Date";

    @Autowired
    EntityManager em;

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public T getById(Long id) {
        return getRepository().findById(id).orElseThrow(() -> new EntityNotFoundException("No se encontro el registro con el id: " + id));
    }

    public T create(T entity){
        return getRepository().save(entity);
    }

    public void delete(Long id) {
        getRepository().deleteById(id);
    }

    public T update(T entity) {
        return getRepository().save(entity);
    }

    public Boolean existsById(Long id) {
        return getRepository().existsById(id);
    }


    public abstract Class<T> getClassType();



    public ListaPaginada<T> findAllPageable(T param, int pageIndex, int pageSize, List<String> orderBy, List<String> orderDir){

        HashMap<String, Object> filtros = null;

        if (param != null) {
            try {
                filtros = this.objectToMap(param);
                this.eliminarNulos(filtros);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(getClassType());
        Root<T> root = criteriaQuery.from(getClassType());
        criteriaQuery.select(root);

        if(filtros != null){
            criteriaQuery.where(generateWhere(cb, root, filtros));
        }
        criteriaQuery.orderBy(getOrderList(cb, root, orderBy, orderDir));
        List<T> result = null;

        try {
            if ((pageSize != -1) && (pageIndex < 1)) {
                throw new Exception("Si el parametro cantidad es distinto a 'null' " +
                        "y '-1'; el parametro pagina es obligatorio y debe ser mayor a '0'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (pageSize > 0) {
            result = em
                    .createQuery(criteriaQuery)
                    .setMaxResults(pageSize)
                    .setFirstResult((pageIndex - 1) * pageSize)
                    .getResultList();
        } else {
            result = em
                    .createQuery(criteriaQuery)
                    .getResultList();
        }


        Long total = countAll(filtros);

        ListaPaginada<T> retorno = new ListaPaginada<>();
        retorno.setLista(result);
        retorno.setTotal(total);

        return retorno;
    }

    public Long countAll(HashMap<String, Object> filtros) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(getClassType());
        criteriaQuery.select(cb.count(root));

        if(filtros != null) {
            criteriaQuery.where(generateWhere(cb, root, filtros));
        }

        return em.createQuery(criteriaQuery).getSingleResult();
    }

    public HashMap<String, String> getOperationForFindAll() {
        return new HashMap<>();
    }

    public Predicate[] generateWhere(CriteriaBuilder cb, Root<T> root, HashMap<String, Object> filtros) {
        List<Predicate> predicatesList = new ArrayList<>();

        filtros.forEach((k, v) -> {

            Path<String> varCondicion = null;
            String[] varCondicionArray = k.split("\\.");

            for (String s : varCondicionArray) {
                if (varCondicion == null) {
                    varCondicion = root.get(s);
                } else {
                    varCondicion = varCondicion.get(s);
                }
            }

            String operacion = getOperationForFindAll().get(k);
            if (operacion != null) {
                switch (operacion) {
                    case (WhereOperationConstant.LIKE):
                        predicatesList.add(cb.like(varCondicion, v.toString()));
                        break;
                    case WhereOperationConstant.FULL_LIKE:
                        predicatesList.add(cb.like(varCondicion, "%" + v.toString() + "%"));
                        break;
                    case WhereOperationConstant.NOT_LIKE:
                        predicatesList.add(cb.notLike(varCondicion, v.toString()));
                        break;

                    case WhereOperationConstant.DISTINTC:
                        predicatesList.add(cb.notEqual(varCondicion, v));
                        break;

                    case WhereOperationConstant.EQUALS:
                        predicatesList.add(cb.equal(varCondicion, v));
                        break;

                    case WhereOperationConstant.IS_NULL:
                        predicatesList.add(cb.isNull(varCondicion));
                        break;

                    case WhereOperationConstant.NOT_NULL:
                        predicatesList.add(cb.isNotNull(varCondicion));
                        break;

                    case WhereOperationConstant.GREATER_THAN:
                        if (varCondicion.getJavaType().toString().equals(DATE_CLASS)) {
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(v.toString());
                                predicatesList.add(cb.greaterThan(varCondicion.as(Date.class), date));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException("La variable: " + k + " no coincide con un formato de fecha valida.");
                            }
                        }
                        else if(varCondicion.getJavaType().equals(Timestamp.class)){
                            if(v instanceof Date){
                                Date fecha = (Date) v;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String fechaComoCadena = sdf.format(v);
                                Timestamp buscar = new Timestamp(fecha.getTime());
                                predicatesList.add(cb.greaterThan(varCondicion.as(Timestamp.class), buscar));
                            }
                        }
                        else if (varCondicion.getJavaType().toString().equals(BIG_DECIMAL_CLASS)) {
                            BigDecimal varValue = new BigDecimal(v.toString());
                            predicatesList.add(cb.greaterThan(varCondicion.as(BigDecimal.class), varValue));
                        } else {
                            predicatesList.add(cb.greaterThan(varCondicion, v.toString()));
                        }
                        break;

                    case WhereOperationConstant.GREATER_THAN_OR_EQUALS:

                        if (varCondicion.getJavaType().toString().equals(DATE_CLASS)) {
                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Ajustar formato si es necesario
                                Date date = dateFormat.parse(v.toString());
                                predicatesList.add(cb.greaterThanOrEqualTo(varCondicion.as(Date.class), date));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException("La variable: " + k + " no coincide con un formato de fecha valida.");
                            }
                        }
                        else if(varCondicion.getJavaType().equals(Timestamp.class)){
                            if(v instanceof Date){
                                Date fecha = (Date) v;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String fechaComoCadena = sdf.format(v);
                                Timestamp buscar = new Timestamp(fecha.getTime());
                                predicatesList.add(cb.greaterThanOrEqualTo(varCondicion.as(Timestamp.class), buscar));
                            }
                        }
                        else if (varCondicion.getJavaType().toString().equals(BIG_DECIMAL_CLASS)) {
                            BigDecimal varValue = new BigDecimal(v.toString());
                            predicatesList.add(cb.greaterThanOrEqualTo(varCondicion.as(BigDecimal.class), varValue));
                        } else {
                            predicatesList.add(cb.greaterThanOrEqualTo(varCondicion, v.toString()));
                        }
                        break;

                    case WhereOperationConstant.LESS_THAN:
                        if (varCondicion.getJavaType().toString().equals(DATE_CLASS)) {
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(v.toString());
                                predicatesList.add(cb.lessThan(varCondicion.as(Date.class), date));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException("La variable: " + k + " no coincide con un formato de fecha valida.");
                            }
                        }
                        else if(varCondicion.getJavaType().equals(Timestamp.class)){
                            if(v instanceof Date){
                                Date fecha = (Date) v;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String fechaComoCadena = sdf.format(v);
                                Timestamp buscar = new Timestamp(fecha.getTime());
                                predicatesList.add(cb.lessThan(varCondicion.as(Timestamp.class), buscar));
                            }
                        }else if (varCondicion.getJavaType().toString().equals(BIG_DECIMAL_CLASS)) {
                            BigDecimal varValue = new BigDecimal(v.toString());
                            predicatesList.add(cb.lessThan(varCondicion.as(BigDecimal.class), varValue));
                        } else {
                            predicatesList.add(cb.lessThan(varCondicion, v.toString()));
                        }
                        break;

                    case WhereOperationConstant.LESS_THAN_OR_EQUALS:
                        if (varCondicion.getJavaType().toString().equals(DATE_CLASS)) {
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(v.toString());
                                System.out.println(date);
                                predicatesList.add(cb.lessThanOrEqualTo(varCondicion.as(Date.class), date));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException("La variable: " + k + " no coincide con un formato de fecha valida.");
                            }
                        }
                        else if(varCondicion.getJavaType().equals(Timestamp.class)){

                            if(v instanceof Date){
                                Date fecha = (Date) v;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String fechaComoCadena = sdf.format(v);
                                Timestamp buscar = new Timestamp(fecha.getTime());
                                predicatesList.add(cb.lessThanOrEqualTo(varCondicion.as(Timestamp.class), buscar));
                            }
                        }
                        else if (varCondicion.getJavaType().toString().equals(BIG_DECIMAL_CLASS)) {
                            BigDecimal varValue = new BigDecimal(v.toString());
                            predicatesList.add(cb.lessThanOrEqualTo(varCondicion.as(BigDecimal.class), varValue));
                        } else {
                            predicatesList.add(cb.lessThanOrEqualTo(varCondicion, v.toString()));
                        }
                        break;

                    default:
                        predicatesList.add(cb.equal(varCondicion, v));
                        break;
                }


            } else {

                predicatesList.add(cb.equal(varCondicion, v));
            }
        });
        Predicate[] finalPredicates = new Predicate[predicatesList.size()];
        predicatesList.toArray(finalPredicates);
        return finalPredicates;
    }

    public List<Order> getOrderList(CriteriaBuilder cb, Root<T> root, List<String> orderBy, List<String> orderDir) {
        List<Order> orderList = new ArrayList();
        AtomicInteger counter = new AtomicInteger(0);
        orderBy.stream().forEach(order -> {
            String[] orderByArray = order.split("\\.");
            Path<String> orderByClause = null;
            for (String s : orderByArray) {
                if (orderByClause == null) {
                    orderByClause = root.get(s);
                } else {
                    orderByClause = orderByClause.get(s);
                }
            }

            if (orderDir.get(counter.get()).equalsIgnoreCase("DESC")) {
                orderList.add(cb.desc(orderByClause));
            } else {
                orderList.add(cb.asc(orderByClause));
            }
            counter.getAndIncrement();
        });
        return orderList;
    }

    public HashMap<String, Object> objectToMap(Object obj) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HashMap<String, Object> result = new HashMap<String, Object>();
        BeanInfo info = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] var4 = info.getPropertyDescriptors();
        int var5 = var4.length;

        for (int i = 0; i < var5; ++i) {
            PropertyDescriptor pd = var4[i];
            Method reader = pd.getReadMethod();
            if (reader != null && !pd.getName().equals("class")) {
                result.put(pd.getName(), reader.invoke(obj));
            }
        }
        return result;
    }


    public void eliminarNulos(Map<String, Object> parametros) {
        Iterator<String> claves = parametros.keySet().iterator();
        String key = null;
        ArrayList<String> listaEliminar = new ArrayList<String>();

        while (claves.hasNext()) {
            key = claves.next();
            if (parametros.get(key) == null && !this.esClaveProtegida(key)) {
                listaEliminar.add(key);
            }
        }

        Iterator<String> iteratorEliminar = listaEliminar.iterator();

        while (iteratorEliminar.hasNext()) {
            String claveElim = (String) iteratorEliminar.next();
            parametros.remove(claveElim);
        }
    }

    private boolean esClaveProtegida(String key) {
        return "filtros".equals(key) || "cantidad".equals(key) || "pagina".equals(key) || "orderBy".equals(key) || "orderDir".equals(key);
    }


}