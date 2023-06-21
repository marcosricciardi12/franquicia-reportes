package com.reporte.models;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long myid;
    private long idreporte;
    private String tipo;
    private Instant fechaInicio;
    private Instant fechaFin;
    private String intervalo;
    private Long reporteCanceladoId;
    
}
