package com.reporte.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioReporte {

    private String accion = "respuesta_reporte";
    private List<DetalleVenta> datos;


}
