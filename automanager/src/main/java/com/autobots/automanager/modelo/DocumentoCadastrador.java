package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import org.springframework.stereotype.Component;

import javax.swing.text.Document;
import java.util.List;

@Component
public class DocumentoCadastrador {
    private StringVerificadorNulo verificadorNulo = new StringVerificadorNulo();

    public void cadastro(Cliente cliente, Documento documento){
        if (documento != null){
            if (!verificadorNulo.verificar(documento.getTipo()) && !verificadorNulo.verificar(documento.getNumero())){
                Documento documentoNovo = new Documento();
                documentoNovo.setTipo(documento.getTipo());
                documentoNovo.setNumero(documento.getNumero());
                cliente.getDocumentos().add(documentoNovo);
            }
        }
    }

    public void cadastro(Cliente cliente, List<Documento> documentos){
        for (Documento documento : documentos){
            cadastro(cliente, documento);
        }
    }
}
