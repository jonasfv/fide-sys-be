package com.arquiweb.fide_sys_be.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arquiweb.fide_sys_be.constant.WhereOperationConstant;
import com.arquiweb.fide_sys_be.entity.BolsaPunto;
import com.arquiweb.fide_sys_be.entity.Cliente;
import com.arquiweb.fide_sys_be.entity.ParametrizacionVecimientoPunto;
import com.arquiweb.fide_sys_be.entity.ReglaPunto;
import com.arquiweb.fide_sys_be.repository.BolsaPuntoRepository;

@Service
public class BolsaPuntoService extends BaseService<BolsaPunto> {

    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    @Autowired
    BolsaPuntoRepository bolsaPuntoRepository;

    @Autowired
    ReglaPuntoService reglaPuntoService;

    @Autowired
    ClienteService clienteService;

    @Autowired
    ParametrizacionVecimientoPuntoService paramService;

    @Override
    public BolsaPuntoRepository getRepository() {
        return bolsaPuntoRepository;
    }

    @Override
    public Class<BolsaPunto> getClassType() {
        return BolsaPunto.class;
    }

    @Override
    public HashMap<String, String> getOperationForFindAll() {

        HashMap<String, String> operation = new HashMap<>();
        operation.put("nombre", WhereOperationConstant.FULL_LIKE);
        operation.put("apellido", WhereOperationConstant.FULL_LIKE);

        return operation;
    }

    public int obtenerPuntosPorRango(int monto) {
        LOGGER.info("'obtener'");
        List<ReglaPunto> listaRegla = reglaPuntoService.findAll();
        LOGGER.info(listaRegla);
        int puntos = 0;
        for (ReglaPunto reglaPunto : listaRegla) {

            if (monto >= reglaPunto.getLimiteInferior() && monto <= reglaPunto.getLimiteSuperior()) {
               
                double puntosDecimales = monto / reglaPunto.getMontoPorPunto();
                LOGGER.info(puntosDecimales);
                puntos = (int) Math.round(puntosDecimales);
                LOGGER.info(puntosDecimales);

            } else if (reglaPunto.getLimiteInferior() == 0 && reglaPunto.getLimiteSuperior() == 0) {

                double puntosDecimales = monto / reglaPunto.getMontoPorPunto();
                LOGGER.info("decimal " + puntosDecimales);
                puntos = (int) Math.round(puntosDecimales);
                LOGGER.info(puntosDecimales);

            }

        }

        return puntos;
    }

    public BolsaPunto guardarConPuntos(long clienteId, int monto) {
        LOGGER.info("que hay" + monto);
        int puntos = obtenerPuntosPorRango(monto);
        BolsaPunto bolsaPunto = new BolsaPunto();

        Cliente clientPunto = clienteService.getById(clienteId);

        List<ParametrizacionVecimientoPunto> paramVencimiento = paramService.findAll();

        int dias = paramVencimiento.get(0).getDiasDuracion();

        LocalDate date = LocalDate.now();

        bolsaPunto.setCliente(clientPunto);
        bolsaPunto.setMontoOperacion(monto);
        bolsaPunto.setPuntajeAsignado(puntos);
        bolsaPunto.setSaldo(puntos);
        bolsaPunto.setPuntajeUtilizado(0);
        bolsaPunto.setFechaAsignacion(date);
        bolsaPunto.setFechaCaducidad(date.plusDays(dias));

        
        return bolsaPuntoRepository.save(bolsaPunto);
    }



    public List<BolsaPunto> findBolsaCliente(Long id){

       return  bolsaPuntoRepository.findByClienteId(id);
    }

    public List<BolsaPunto> findBolsaCaducada(){

        LocalDate hoy = LocalDate.now();
        List<BolsaPunto> listaBolsa = bolsaPuntoRepository.findByFechaCaducidad(hoy);

        return  listaBolsa;
     }

     public List<BolsaPunto> findBolsaDias(int dias){

        LocalDate fecha = LocalDate.now().minusDays(dias);
        List<BolsaPunto> listaBolsa = bolsaPuntoRepository.findByFechaCaducidad(fecha);

        return  listaBolsa;
     }
}
