package com.reporte.services;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import com.reporte.models.FechaDatos;
import com.reporte.models.Reporte;
import com.reporte.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.reporte.models.ReporteRecurrente;
import com.reporte.repository.ReporteRecurrenteRepository;

@Service
@Component
@Transactional
public class ReporteRecurrenteService {
	
	private Reporte reporteRaH;
	private FechaDatos intervalo;
	
	public synchronized void hilo_recurrente(
			ReporteRecurrente reporte, 
			@Autowired 
			ReporteRecurrenteRepository reporterecurrenteRepository,
			@Autowired 
			ReporteRepository reporteRepository,
			@Autowired
			DataEnviarReporteService dataEnviarReporteService) {
		
		Instant fecha_inicio = reporte.getFechaInicio();
		Instant fecha_fin = reporte.getFechaFin();
		Duration intervalo = Duration.parse(reporte.getIntervalo());
		
		while (!reporte.getCancelado()) {
			Instant fecha_actual_general = Instant.now();
			ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");
			//System.out.println("ZONA HORARIO ELEGIDA: " + zoneId);
			ZonedDateTime fecha_actual_zoneARG = ZonedDateTime.ofInstant(fecha_actual_general, zoneId);
			Instant fecha_actual = fecha_actual_zoneARG.toInstant().minus(4, ChronoUnit.HOURS);
			//System.out.println("fecha actual SEGUN ZONEARG: " + fecha_actual_zoneARG);
			//System.out.println("fecha actual SEGUN Instant: " + fecha_actual);
			reporte =  reporterecurrenteRepository.findById(reporte.getMyid()).get();
			System.out.println("\nPID HILO recurrente: " + Thread.currentThread().getId() + 
								"\nReporte RECURRENTE id: " + reporte.getIdreporte() +
								"\nDURACION DEL INTERVALO en segundos: " + intervalo.getSeconds() +
								"\nIntervalo: " + reporte.getIntervalo() +
								"\nEstadoCancelado: " + reporte.getCancelado() + 
								"\nFecha inicio: " + fecha_inicio +
								"\nFecha final: " + fecha_fin +
								"\nFECHA ACTUAL: " + fecha_actual + "\n");
			if (reporte.getCancelado()) {
				break;
			}
			if (fecha_inicio.isBefore(fecha_actual) & fecha_fin.isAfter(fecha_actual)) {
				System.out.println("\nSolicitando datos del periodo indicado!\n");
				this.intervalo = new FechaDatos();
				this.intervalo.setFechaFin(fecha_actual);
				this.intervalo.setFechaInicio(fecha_actual.minus(intervalo.getSeconds(), ChronoUnit.SECONDS));
				dataEnviarReporteService.pedirdatos(this.intervalo);
				 
			}
			else {
				if (fecha_fin.isBefore(fecha_actual)) {
					this.saveRah(reporte, reporteRepository);
					this.deleteByIdreporte(reporte.getIdreporte(), reporterecurrenteRepository);
					System.out.println("\nTERMINADO EL RECURRENTE id: " + reporte.getIdreporte() +  " !\n");
					break;
				}
				if (fecha_inicio.isAfter(fecha_actual)) {
					System.out.println("\nAun no es momento de enviar el reporte recurrente id: " + 
							reporte.getIdreporte() +  " !\n");
				}
				
			}
			try {
			    Thread.sleep(intervalo.getSeconds() * 1000);
			} catch(InterruptedException e) {
			    System.out.println("got interrupted!");
			}
		}
		
		System.out.println("\nREPORTE RECURRENTE ID: " + reporte.getIdreporte() + " TERMINADO!\n");
		if (reporte.getCancelado()) {
			System.out.println("\nEL REPORTE RECURRENTE ID: " + reporte.getIdreporte() + " FUE CANCELADO\n");
		}
		this.deleteByIdreporte(reporte.getIdreporte(),reporterecurrenteRepository);
	}
	
	@Transactional
	public Boolean deleteByIdreporte(Long id, @Autowired ReporteRecurrenteRepository reporterecurrenteRepository) {
    	System.out.println(id);
    	
		if (reporterecurrenteRepository.findByIdreporte(id).isEmpty()) {
			System.out.println("No encontre ese reporte");
			return false;
		}
		reporterecurrenteRepository.deleteByIdreporte(id);
		return !reporterecurrenteRepository.findByIdreporte(id).isPresent();
	}
	public void saveRah(ReporteRecurrente reporterecurrente, @Autowired ReporteRepository reporteRepository){
		/*Esta funcion es para pasar el reporte recurrente que TERMINA SOLO a la tabla 
		  con el registro de reportes*/
		this.reporteRaH = new Reporte();
		this.reporteRaH.setFechaFin(reporterecurrente.getFechaFin());
		this.reporteRaH.setFechaInicio(reporterecurrente.getFechaInicio());
		this.reporteRaH.setIdreporte(reporterecurrente.getIdreporte());
		this.reporteRaH.setIntervalo(reporterecurrente.getIntervalo());
		this.reporteRaH.setTipo(reporterecurrente.getTipo());
		reporteRepository.save(this.reporteRaH);
	}
}


