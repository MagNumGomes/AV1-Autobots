package com.autobots.automanager.controles;

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
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/cliente/{clienteId}/endereco")
public class EnderecoControle {
    private final ClienteRepositorio repositorio;

    public EnderecoControle(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public Endereco obter(@PathVariable Long clienteId) {
        return obterEndereco(buscarCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<Endereco> cadastrar(@PathVariable Long clienteId, @Valid @RequestBody Endereco endereco) {
        Cliente cliente = buscarCliente(clienteId);
        if (cliente.getEndereco() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cliente já possui endereço");
        }
        endereco.setId(null);
        cliente.setEndereco(endereco);
        Endereco enderecoSalvo = repositorio.saveAndFlush(cliente).getEndereco();
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoSalvo);
    }

    @PutMapping
    public Endereco atualizar(@PathVariable Long clienteId, @Valid @RequestBody Endereco atualizacao) {
        Cliente cliente = buscarCliente(clienteId);
        Endereco endereco = obterEndereco(cliente);
        endereco.setEstado(atualizacao.getEstado());
        endereco.setCidade(atualizacao.getCidade());
        endereco.setBairro(atualizacao.getBairro());
        endereco.setRua(atualizacao.getRua());
        endereco.setNumero(atualizacao.getNumero());
        endereco.setCodigoPostal(atualizacao.getCodigoPostal());
        endereco.setInformacoesAdicionais(atualizacao.getInformacoesAdicionais());
        return repositorio.save(cliente).getEndereco();
    }

    @DeleteMapping
    public ResponseEntity<Void> excluir(@PathVariable Long clienteId) {
        Cliente cliente = buscarCliente(clienteId);
        obterEndereco(cliente);
        cliente.setEndereco(null);
        repositorio.save(cliente);
        return ResponseEntity.noContent().build();
    }

    private Cliente buscarCliente(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    private Endereco obterEndereco(Cliente cliente) {
        if (cliente.getEndereco() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado");
        }
        return cliente.getEndereco();
    }
}
