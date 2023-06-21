package com.reporte.controller;

import com.reporte.services.DataEnviarReporteService;
import com.reporte.services.ReporteService;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reporte.models.FechaDatos;
import com.reporte.models.Reporte;
import com.reporte.models.ReporteJSON;

@RestController
@RequestMapping(("/api/reporte"))
public class ReporteController {

	private FechaDatos intervalo;
	private Reporte reporte;
	@Autowired
	ReporteService reporteService;
	@Autowired
	DataEnviarReporteService enviarReporteService;
	 @PostMapping("/")
	    public ResponseEntity<HttpStatus> newReporte(@RequestBody ReporteJSON reporteJSON){
		 System.out.println("HAY QUE HACER UN HISTORICO //CONTROLLER");
		 this.reporte = new Reporte();
		 this.reporte.setFechaFin(reporteJSON.getFechaFin());
		 this.reporte.setFechaInicio(reporteJSON.getFechaInicio());
		 this.reporte.setIdreporte(reporteJSON.getId());
		 this.reporte.setIntervalo(reporteJSON.getIntervalo());
		 this.reporte.setReporteCanceladoId(reporteJSON.getReporteCanceladoId());
		 this.reporte.setTipo(reporteJSON.getTipo());
		 reporteService.reporteSave(this.reporte);
		 
		 if(this.reporte.getTipo().equals("historico")) {
			 this.intervalo = new FechaDatos();
			 this.intervalo.setFechaFin(this.reporte.getFechaFin());
			 this.intervalo.setFechaInicio(this.reporte.getFechaInicio());
			 enviarReporteService.pedirdatos(this.intervalo);
			 
			
		 }
		 
		 if(this.reporte.getTipo().equals("recurrente")) {
			 System.out.println("HAY QUE HACER UN RECURRENTE //CONTROLLER, de esto se encarga el hilo");
			 
			 
			
		 }
	        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	    }
}
