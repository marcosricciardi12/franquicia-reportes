package com.reporte.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.reporte.models.ReporteRecurrente;
import com.reporte.repository.ReporteRecurrenteRepository;
import com.reporte.repository.ReporteRepository;
import com.reporte.services.DataEnviarReporteService;
import com.reporte.services.ReporteRecurrenteAsyncService;

@Component
public class Recurrente_startupComponent {
	
	@Autowired
	DataEnviarReporteService dataEnviarReporteService;
	@Autowired
	ReporteRecurrenteAsyncService reporteRecurrenteAsyncService;
    @Autowired
    ReporteRepository reporteRepository;
    @Autowired
    ReporteRecurrenteRepository reporterecurrenteRepository;
    
    private List<ReporteRecurrente> lista_recurrentes;
	
	@EventListener(ApplicationReadyEvent.class)
	public void start_recurrentes() {
		this.lista_recurrentes = reporterecurrenteRepository.findAll();
		
		for (ReporteRecurrente reporterecurrente : this.lista_recurrentes) {
			this.reporteRecurrenteAsyncService.exec(reporterecurrente, reporterecurrenteRepository, reporteRepository, dataEnviarReporteService);
		}
		
	}

}
