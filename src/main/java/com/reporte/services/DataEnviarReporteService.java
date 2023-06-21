package com.reporte.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.reporte.models.Datos;
import com.reporte.models.EnvioReporte;
import com.reporte.models.FechaDatos;
import com.reporte.models.Loggin;
import com.reporte.models.RespuestaReporte;
import com.reporte.models.User;

import reactor.core.publisher.Mono;

@Service
@Component
public class DataEnviarReporteService {
	
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
	private EnvioReporte envioReporte;
	private Datos datos_inf;
	private RespuestaReporte respuestaServicioPrincipal;
	
    
	public void pedirdatos(FechaDatos intervalo) {

		System.out.println("\nSolicitando ventas al servicio franquicia entre " + intervalo.getFechaInicio() + 
							" y " + intervalo.getFechaFin());
    	WebClient webClient = WebClient.builder()
                .baseUrl(url+"/api/reporte/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<Datos> responseJson = webClient.post()
                .body(Mono.just(intervalo), FechaDatos.class)
                .retrieve().bodyToMono(Datos.class);
        
        this.datos_inf = responseJson.block();
		this.envioReporte = new EnvioReporte();
		this.envioReporte.setDatos(this.datos_inf.getDetalleventa());
		if (!this.envioReporte.getDatos().isEmpty()) {
			System.out.println("\nENVIANDO REPORTE al servidor principal: \n ////\n" + 
					"\n\t\tAccion:" + this.envioReporte.getAccion() +
					"\n\t\tDatos:" + this.envioReporte.getDatos() + 
					"\n\n/////\n");
			this.sendReporte(envioReporte);
		}
		else {
			System.out.println("\nNO HUBO VENTAS EN EL PERIODO SOLICITADO!\n");
		}
		
		


    }

	public String loggin() {
		WebClient webClient = WebClient.builder()
				.baseUrl(url1+path1)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();

		User body = new User();
		body.setUsername(user);
		body.setPassword(pass);

		Mono<Loggin> responseJson = webClient.post()
				.body(Mono.just(body), User.class)
				.retrieve().bodyToMono(Loggin.class);
		this.tokenid = responseJson.block().getId_token();
		return this.tokenid;
	}
	
	public void sendReporte(EnvioReporte envioReporte) {
		WebClient webClient = WebClient.builder()
				.baseUrl(url1+pathEnvio)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();

		Mono<RespuestaReporte> responseJson = webClient.post()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + this.loggin() + "\"")
				.body(Mono.just(envioReporte), EnvioReporte.class)
				.retrieve().bodyToMono(RespuestaReporte.class);
		
		this.respuestaServicioPrincipal = new RespuestaReporte();
		this.respuestaServicioPrincipal = responseJson.block();
		
		System.out.println("\nRespuesta (JSON) Servicio principal al recibir reporte: \n" +
							"\n\t Accion: " + this.respuestaServicioPrincipal.getAccion() +
							"\n\t Estado: " + this.respuestaServicioPrincipal.getEstado() +
							"\n\t EstadoDTO: " + this.respuestaServicioPrincipal.getDatoReportadoEstadoDTO() + 
							"\n");
	}
}
