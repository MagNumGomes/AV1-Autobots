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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/cliente/{clienteId}/telefones")
public class TelefoneControle {
    private final ClienteRepositorio repositorio;

    public TelefoneControle(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<Telefone> listar(@PathVariable Long clienteId) {
        return buscarCliente(clienteId).getTelefones();
    }

    @GetMapping("/{id}")
    public Telefone obter(@PathVariable Long clienteId, @PathVariable Long id) {
        return buscarTelefone(buscarCliente(clienteId), id);
    }

    @PostMapping
    public ResponseEntity<Telefone> cadastrar(@PathVariable Long clienteId, @Valid @RequestBody Telefone telefone) {
        Cliente cliente = buscarCliente(clienteId);
        telefone.setId(null);
        cliente.getTelefones().add(telefone);
        Cliente salvo = repositorio.saveAndFlush(cliente);
        Telefone telefoneSalvo = salvo.getTelefones().get(salvo.getTelefones().size() - 1);
        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneSalvo);
    }

    @PutMapping("/{id}")
    public Telefone atualizar(@PathVariable Long clienteId, @PathVariable Long id,
                              @Valid @RequestBody Telefone atualizacao) {
        Cliente cliente = buscarCliente(clienteId);
        Telefone telefone = buscarTelefone(cliente, id);
        telefone.setDdd(atualizacao.getDdd());
        telefone.setNumero(atualizacao.getNumero());
        repositorio.save(cliente);
        return telefone;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long clienteId, @PathVariable Long id) {
        Cliente cliente = buscarCliente(clienteId);
        cliente.getTelefones().remove(buscarTelefone(cliente, id));
        repositorio.save(cliente);
        return ResponseEntity.noContent().build();
    }

    private Cliente buscarCliente(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private Telefone buscarTelefone(Cliente cliente, Long id) {
        return cliente.getTelefones().stream().filter(item -> item.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Telefone não encontrado"));
    }
}
