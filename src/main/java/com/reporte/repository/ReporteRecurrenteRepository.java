package com.reporte.repository;

import com.reporte.models.ReporteRecurrente;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRecurrenteRepository extends JpaRepository<ReporteRecurrente, Long> {
	
	
	public Optional<ReporteRecurrente> findByIdreporte(Long id);
	
	@Modifying
	@Transactional
	public void deleteByIdreporte(Long id);
}
