package com.autobots.automanager.controles;

import java.util.List;
import java.util.Date;

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

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.ClienteRepositorio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {
    private final ClienteRepositorio repositorio;

    public ClienteControle(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping({"", "/"})
    public List<Cliente> obterClientes() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public Cliente obterCliente(@PathVariable Long id) {
        return buscar(id);
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody Cliente cliente) {
        cliente.setId(null);
        cliente.setDataCadastro(new Date());
        if (cliente.getDocumentos() != null) cliente.getDocumentos().forEach(documento -> documento.setId(null));
        if (cliente.getTelefones() != null) cliente.getTelefones().forEach(telefone -> telefone.setId(null));
        if (cliente.getEndereco() != null) cliente.getEndereco().setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(cliente));
    }

    @PutMapping("/{id}")
    public Cliente atualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente atualizacao) {
        Cliente cliente = buscar(id);
        cliente.setNome(atualizacao.getNome());
        cliente.setNomeSocial(atualizacao.getNomeSocial());
        cliente.setDataNascimento(atualizacao.getDataNascimento());
        return repositorio.save(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        repositorio.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }

    private Cliente buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}
