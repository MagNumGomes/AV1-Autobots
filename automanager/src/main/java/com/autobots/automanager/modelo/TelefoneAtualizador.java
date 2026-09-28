package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Telefone;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelefoneAtualizador {
	private StringVerificadorNulo verificadorNulo = new StringVerificadorNulo();

	public void atualizar(Telefone telefone, Telefone telefoneNovo){
		if (telefoneNovo != null){
			if (!verificadorNulo.verificar(telefoneNovo.getDdd()) && !verificadorNulo.verificar(telefoneNovo.getNumero())){
				telefone.setDdd(telefoneNovo.getDdd());
				telefone.setNumero(telefoneNovo.getNumero());
			}
		}
	}

	public void atualizar(List<Telefone> telefones, List<Telefone> telefonesNovos){
		for (Telefone telefone : telefones){
			for (Telefone telefoneNovo : telefonesNovos){
				if (telefone.getId().equals(telefoneNovo.getId())){
					atualizar(telefone, telefoneNovo);
				}
			}
		}
	}
}
