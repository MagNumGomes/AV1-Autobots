package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelefoneCadastrar {
    private StringVerificadorNulo verificadorNulo = new StringVerificadorNulo();

    public void cadastro(Cliente cliente, Telefone telefone){
        if (telefone != null){
            if (!verificadorNulo.verificar(telefone.getDdd()) && !verificadorNulo.verificar(telefone.getNumero())){
                Telefone telefoneNovo = new Telefone();
                telefoneNovo.setDdd(telefone.getDdd());
                telefoneNovo.setNumero(telefone.getNumero());
                cliente.getTelefones().add(telefoneNovo);
            }
        }
    }

    public void cadastro(Cliente cliente, List<Telefone> telefones){
        for (Telefone telefone : telefones){
            cadastro(cliente, telefone);
        }
    }
}
