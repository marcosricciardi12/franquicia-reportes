package com.reporte.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Data
@Entity
@Table
@Cacheable(false)
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRecurrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long myid;
    private long idreporte;
    private String tipo;
    private Instant fechaInicio;
    private Instant fechaFin;
    private String intervalo;
    private Boolean cancelado = false;

}
