package com.arquiweb.fide_sys_be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.BolsaPunto;
import com.arquiweb.fide_sys_be.entity.Cliente;
import com.arquiweb.fide_sys_be.entity.ConceptoUso;
import com.arquiweb.fide_sys_be.entity.UsoCabecera;
import com.arquiweb.fide_sys_be.entity.UsoDTO;
import com.arquiweb.fide_sys_be.entity.UsoDetalle;
import com.arquiweb.fide_sys_be.repository.UsoCabeceraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UsoCabeceraService extends BaseService<UsoCabecera> {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Autowired
    UsoCabeceraRepository usoCabeceraRepository;

    @Autowired
    ClienteService clienteService;

    @Autowired
    ConceptoUsoService conceptoUsoService;

    @Autowired
    BolsaPuntoService bolsaPuntoService;

    @Autowired
    UsoDetalleService usoDetalleService;

    @Override
    public UsoCabeceraRepository getRepository() {
        return usoCabeceraRepository;
    }

    @Override
    public Class<UsoCabecera> getClassType() {
        return UsoCabecera.class;
    }

    @Override
    public HashMap<String, String> getOperationForFindAll() {

        HashMap<String, String> operation = new HashMap<>();
        operation.put("nombre", WhereOperationConstant.FULL_LIKE);
        operation.put("apellido", WhereOperationConstant.FULL_LIKE);

        return operation;
    }

    public UsoDTO generarUso(Long clienteId, Long conceptoId) {

        Cliente clientPunto = clienteService.getById(clienteId);

        ConceptoUso conceptoUso = conceptoUsoService.getById(conceptoId);

        List<BolsaPunto> listaBolsa = bolsaPuntoService.findByClienteId(clienteId);

        int aux = 0;
        int puntosRestantes = 0;

        UsoCabecera usoCabecera = new UsoCabecera();
        LocalDate fechaHoy = LocalDate.now();

        usoCabecera.setCliente(clientPunto);
        usoCabecera.setConceptoUso(conceptoUso);
        usoCabecera.setFechaTransaccion(fechaHoy);
        usoCabecera.setPuntosUtilizados(conceptoUso.getPuntos());

        puntosRestantes = conceptoUso.getPuntos();

        List<UsoDetalle> listaDetalle = new ArrayList();

        UsoDTO uso = new UsoDTO();
        uso.setCabecera(usoCabecera);
        UsoCabecera response = usoCabeceraRepository.save(usoCabecera);

        for (BolsaPunto bolsaPunto : listaBolsa) {
            LOGGER.info("puntos restantes " + puntosRestantes);
            if (puntosRestantes <= 0)
                break;

            int saldoDisponible = bolsaPunto.getPuntajeAsignado() - bolsaPunto.getPuntajeUtilizado();
            int puntosUsados = Math.min(saldoDisponible, puntosRestantes);
            LOGGER.info("puntos disponible  " + saldoDisponible);
            LOGGER.info("puntos usado  " + puntosUsados);

            bolsaPunto.setPuntajeUtilizado(bolsaPunto.getPuntajeUtilizado() + puntosUsados);
            bolsaPunto.setSaldo(bolsaPunto.getPuntajeAsignado() - bolsaPunto.getPuntajeUtilizado());
            puntosRestantes -= puntosUsados;

            UsoDetalle usoDetalle = new UsoDetalle();
            usoDetalle.setBolsaPuntos(bolsaPunto);
            usoDetalle.setPuntajetulizado(puntosUsados);
            listaDetalle.add(usoDetalle);

            bolsaPuntoService.update(bolsaPunto);
        }

        if (listaDetalle != null) {
            uso.setDetalles(listaDetalle);
            for (UsoDetalle usoDetalle : listaDetalle) {
                usoDetalle.setCabecera(response);
                usoDetalleService.create(usoDetalle);
            }
        }
        return uso;
    }

    public Page<UsoCabecera> getUsoCabecerasByClienteId(Long clienteId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usoCabeceraRepository.findByClienteId(clienteId, pageable);
    }

    public Page<UsoCabecera> getUsoCabecerasByConceptoUsoId(Long conceptoUsoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usoCabeceraRepository.findByConceptoUsoId(conceptoUsoId, pageable);
    }


    public Page<UsoCabecera> getUsoCabecerasByFechaTransaccion(LocalDate fechaTransaccion, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usoCabeceraRepository.findByFechaTransaccion(fechaTransaccion, pageable);
    }
}
