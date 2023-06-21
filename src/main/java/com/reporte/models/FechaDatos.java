package com.reporte.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FechaDatos {
	
	private Instant fechaInicio;
    private Instant fechaFin;

}
