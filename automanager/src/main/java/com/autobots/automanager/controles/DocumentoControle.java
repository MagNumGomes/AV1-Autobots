package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/cliente/{clienteId}/documentos")
public class DocumentoControle {
    private final ClienteRepositorio repositorio;

    public DocumentoControle(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<Documento> listar(@PathVariable Long clienteId) {
        return buscarCliente(clienteId).getDocumentos();
    }

    @GetMapping("/{id}")
    public Documento obter(@PathVariable Long clienteId, @PathVariable Long id) {
        return buscarDocumento(buscarCliente(clienteId), id);
    }

    @PostMapping
    public ResponseEntity<Documento> cadastrar(@PathVariable Long clienteId, @Valid @RequestBody Documento documento) {
        Cliente cliente = buscarCliente(clienteId);
        documento.setId(null);
        cliente.getDocumentos().add(documento);
        Cliente salvo = repositorio.saveAndFlush(cliente);
        Documento documentoSalvo = salvo.getDocumentos().stream()
                .filter(item -> documento.getNumero().equals(item.getNumero())).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoSalvo);
    }

    @PutMapping("/{id}")
    public Documento atualizar(@PathVariable Long clienteId, @PathVariable Long id,
                               @Valid @RequestBody Documento atualizacao) {
        Documento documento = buscarDocumento(buscarCliente(clienteId), id);
        documento.setTipo(atualizacao.getTipo());
        documento.setNumero(atualizacao.getNumero());
        repositorio.save(buscarCliente(clienteId));
        return documento;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long clienteId, @PathVariable Long id) {
        Cliente cliente = buscarCliente(clienteId);
        cliente.getDocumentos().remove(buscarDocumento(cliente, id));
        repositorio.save(cliente);
        return ResponseEntity.noContent().build();
    }

    private Cliente buscarCliente(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private Documento buscarDocumento(Cliente cliente, Long id) {
        return cliente.getDocumentos().stream().filter(item -> item.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado"));
    }
}
