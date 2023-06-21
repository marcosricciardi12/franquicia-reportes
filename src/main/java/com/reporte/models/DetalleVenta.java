package com.reporte.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DetalleVenta {
	
	private Instant fecha;
	private Long menu;
	private Double precio;
}
