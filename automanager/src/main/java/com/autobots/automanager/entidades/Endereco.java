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
public class Endereco {
	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = true)
	private String estado;
	@Column(nullable = false)
	@NotBlank
	@Size(max = 100)
	private String cidade;
	@Column(nullable = true)
	private String bairro;
	@Column(nullable = false)
	@NotBlank
	@Size(max = 150)
	private String rua;
	@Column(nullable = false)
	@NotBlank
	@Size(max = 20)
	private String numero;
	@Column(nullable = true)
	private String codigoPostal;
	@Column(unique = false, nullable = true)
	private String informacoesAdicionais;

}
