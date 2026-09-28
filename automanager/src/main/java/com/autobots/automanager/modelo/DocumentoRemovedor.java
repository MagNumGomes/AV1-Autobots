package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentoRemovedor {
    private StringVerificadorNulo verificadorNulo = new StringVerificadorNulo();

    public void remover(Cliente cliente, Documento documento) {
        if (documento != null) {
            if (!verificadorNulo.verificar(documento.getTipo()) && !verificadorNulo.verificar(documento.getNumero())){
                cliente.getDocumentos().remove(documento);
            }
        }
    }

    public void remover(Cliente cliente, List<Documento> documentos) {
        for (Documento documentoVazio : documentos) {
            if (documentoVazio.getId() != null) {
                remover(cliente, documentoVazio);
            }
        }
    }
}
