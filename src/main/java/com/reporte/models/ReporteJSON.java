package com.reporte.models;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteJSON {

    private long id;
    private String tipo;
    private Instant fechaInicio;
    private Instant fechaFin;
    private String intervalo;
    private Long reporteCanceladoId;
}
