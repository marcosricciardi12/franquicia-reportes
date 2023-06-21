package com.reporte.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado {
    private String accion;
    private String estado;
    private String errorMotivo;
    private List<Erroneos> erroneos;
}
