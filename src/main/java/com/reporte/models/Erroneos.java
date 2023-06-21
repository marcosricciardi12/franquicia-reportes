package com.reporte.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Erroneos {
    private Instant fecha;
    private Double ventaId;
    private Double menu;
    private Double precio;
}
