package com.support.controller;

import com.support.model.Cliente;
import com.support.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Criar um novo cliente
    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        Cliente clienteSalvo = clienteService.salvarCliente(cliente);
        return new ResponseEntity<>(clienteSalvo, HttpStatus.CREATED); // Retorna 201 Created
    }

    // Listar todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK); // Retorna 200 OK
    }

    // Buscar um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarClientePorId(id);
        return cliente.map(ResponseEntity::ok) // Se encontrado, retorna 200 OK
                      .orElseGet(() -> ResponseEntity.notFound().build()); // Se não encontrado, retorna 404 Not Found
    }

    // Atualizar um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteService.buscarClientePorId(id);
        if (clienteExistente.isPresent()) {
            cliente.setId(id);
            Cliente clienteAtualizado = clienteService.salvarCliente(cliente);
            return ResponseEntity.ok(clienteAtualizado); // Retorna 200 OK com o cliente atualizado
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se o cliente não existir
        }
    }

    // Excluir um cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        if (clienteService.buscarClientePorId(id).isPresent()) {
            clienteService.excluirCliente(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content após exclusão
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 Not Found se o cliente não existir
        }
    }
}
