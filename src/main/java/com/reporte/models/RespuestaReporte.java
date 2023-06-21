package com.reporte.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaReporte {

    private String accion;
    private String estado;
    private Estado datoReportadoEstadoDTO;
}
