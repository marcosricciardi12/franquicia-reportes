package com.reporte.services;

import com.reporte.models.*;
import com.reporte.repository.ReporteRecurrenteRepository;
import com.reporte.repository.ReporteRepository;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Component
@Service
public class ReporteService {

	private String pathEnvio = "/api/reporte/datos";
	private String tokenid;
	@Value("${loggin.user}")
	private String user;
	@Value("${loggin.pass}")
	private String pass;
	String path1 = "/api/authenticate";
	@Value("${loggin.urlventas}")
    private String url;

	@Value("${loggin.url}")
	private String url1;
	private String token;
	private LogginService login;
	private ReporteRecurrente reporterecurrente;
	private Optional<ReporteRecurrente> reporterecurrenteDEL;
	private Reporte reporteRaH;

	private EnvioReporte envioReporte;

	private Datos datos_inf;
	@Autowired
	DataEnviarReporteService dataEnviarReporteService;
	
	@Autowired
	ReporteRecurrenteAsyncService reporteRecurrenteAsyncService;
    @Autowired
    ReporteRepository reporteRepository;
    @Autowired
    ReporteRecurrenteRepository reporterecurrenteRepository;
    
    @Transactional
    public void reporteSave(Reporte reporte) {
    	System.out.println("\nGenerar reporte y guardar registro en la BD: \n" + reporte);
    	System.out.println("\nTipo de reporte: " + reporte.getTipo());
    	if (reporte.getTipo().equals("historico")) {
    		System.out.println("\nHay que generar y guardar un reporte historico");
    		reporteRepository.save(reporte);
    	}
    	if (reporte.getTipo().equals("recurrente")) {
    		System.out.println("\nHay que generar y guardar un reporte recurrente\n");
    		this.reporterecurrente = new ReporteRecurrente();
    		this.reporterecurrente.setFechaFin(reporte.getFechaFin());
    		this.reporterecurrente.setFechaInicio(reporte.getFechaInicio());
    		this.reporterecurrente.setIdreporte(reporte.getIdreporte());
    		this.reporterecurrente.setIntervalo(reporte.getIntervalo());
    		this.reporterecurrente.setTipo(reporte.getTipo());
    		reporterecurrenteRepository.save(this.reporterecurrente);
    		this.reporteRecurrenteAsyncService.exec(this.reporterecurrente, reporterecurrenteRepository, reporteRepository, dataEnviarReporteService);
    	}
    	if (reporte.getTipo().equals("cancelar")) {
    		System.out.println("\nHay que cancelar el reporte ID " + reporte.getReporteCanceladoId());
    		this.reporterecurrente = new ReporteRecurrente();
    		//Busco el reporte recurrente y cambio el estado a cancelado
        	this.reporterecurrente = reporterecurrenteRepository.findByIdreporte(reporte.getReporteCanceladoId()).get();
        	this.reporterecurrente.setCancelado(true);
        	reporterecurrenteRepository.save(this.reporterecurrente);
        	reporterecurrenteRepository.flush();
        	//Paso el reporte recurrente a la tabla de registros de reportes solicitados
			this.reporteRaH = new Reporte();
			this.reporteRaH.setFechaFin(this.reporterecurrente.getFechaFin());
			this.reporteRaH.setFechaInicio(this.reporterecurrente.getFechaInicio());
			this.reporteRaH.setIdreporte(this.reporterecurrente.getIdreporte());
			this.reporteRaH.setIntervalo(this.reporterecurrente.getIntervalo());
			this.reporteRaH.setTipo(this.reporterecurrente.getTipo());
			reporteRepository.save(this.reporteRaH);
    		
    	}

    }
    
    
    
    

}
