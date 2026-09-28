package com.autobots.automanager.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Documento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	@NotBlank
	@Size(max = 40)
	private String tipo;
	@Column(unique = true, nullable = false)
	@NotBlank
	@Size(max = 80)
	private String numero;
}
