package com.reporte.services;

import com.reporte.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.reporte.models.ReporteRecurrente;
import com.reporte.repository.ReporteRecurrenteRepository;

/*Servicio que al ser llamado, incluye la funcion async
  de forma que la funcion asincrona pueda hacer los transactional*/
@Service
@EnableAsync
public class ReporteRecurrenteAsyncService {
	
	private ReporteRecurrenteService reporteService;
	
	@Async
	public void exec(ReporteRecurrente reporte, 
			@Autowired 
			ReporteRecurrenteRepository reporterecurrenteRepository,
			@Autowired 
			ReporteRepository reporteRepository,
			@Autowired
			DataEnviarReporteService dataEnviarReporteService) {
		
		this.reporteService = new ReporteRecurrenteService();
		this.reporteService.hilo_recurrente(reporte, reporterecurrenteRepository, reporteRepository, dataEnviarReporteService);
	}

}
