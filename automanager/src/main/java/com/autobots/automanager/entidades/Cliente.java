package com.autobots.automanager.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import lombok.Data;

@Data
@Entity
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	@NotBlank
	@Size(max = 120)
	private String nome;
	@Column
	@Size(max = 120)
	private String nomeSocial;
	@Column
	private Date dataNascimento;
	@Column
	private Date dataCadastro;
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@Valid
	private List<Documento> documentos = new ArrayList<>();
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@Valid
	private Endereco endereco;
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@Valid
	private List<Telefone> telefones = new ArrayList<>();

}
