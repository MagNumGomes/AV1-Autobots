package com.autobots.automanager.modelo;

import ch.qos.logback.core.net.server.Client;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelefoneRemovedor {
    private StringVerificadorNulo verificadorNulo = new StringVerificadorNulo();

    public void remover(Cliente cliente, Telefone telefone){
        if (telefone != null){
            if (!verificadorNulo.verificar(telefone.getDdd()) && !verificadorNulo.verificar(telefone.getNumero())){
                cliente.getTelefones().remove(telefone);
            }
        }
    }

    public void remover(Cliente cliente, List<Telefone> telefones){
        for (Telefone telefoneVazio : telefones){
            if (telefoneVazio.getId() != null){
                remover(cliente, telefoneVazio);
            }
        }
    }
}
