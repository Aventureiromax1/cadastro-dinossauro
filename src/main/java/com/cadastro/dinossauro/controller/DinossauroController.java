package com.cadastro.dinossauro.controller;

import com.cadastro.dinossauro.model.Dinossauro;
import com.cadastro.dinossauro.repository.DinossauroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping({"/dinossauro", "/dinossauros"})
@CrossOrigin(origins = "*")
public class DinossauroController {

    @Autowired
    private DinossauroRepository dinossauroRepository;

    @GetMapping
    public List<Dinossauro> listar(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            return dinossauroRepository.findByNomeContainingIgnoreCase(nome.trim());
        }
        return dinossauroRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dinossauro> buscarPorId(@PathVariable Long id) {
        return dinossauroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Dinossauro>> buscarPorNome(@PathVariable String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Dinossauro> encontrados = dinossauroRepository.findByNomeContainingIgnoreCase(nome.trim());
        if (encontrados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(encontrados);
    }

    @PostMapping
    public ResponseEntity<Dinossauro> cadastrar(@RequestBody Dinossauro dinossauro) {
        if (dinossauro == null || dinossauro.getNome() == null || dinossauro.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // Evita sobrescrever registro existente caso venha id preenchido no POST
        dinossauro.setId(null);
        if (dinossauro.getDataCriacao() == null) {
            dinossauro.setDataCriacao(LocalDateTime.now());
        }
        Dinossauro salvo = dinossauroRepository.save(dinossauro);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dinossauro> atualizar(@PathVariable Long id, @RequestBody Dinossauro dadosAtualizados) {
        if (dadosAtualizados == null) {
            return ResponseEntity.badRequest().build();
        }
        return dinossauroRepository.findById(id)
                .map(existente -> {
                    existente.setNome(dadosAtualizados.getNome() != null && !dadosAtualizados.getNome().trim().isEmpty() 
                            ? dadosAtualizados.getNome().trim() 
                            : existente.getNome());
                    existente.setEspecie(dadosAtualizados.getEspecie() != null 
                            ? dadosAtualizados.getEspecie() 
                            : existente.getEspecie());
                    existente.setPeso(dadosAtualizados.getPeso() != null 
                            ? dadosAtualizados.getPeso() 
                            : existente.getPeso());
                    existente.setAltura(dadosAtualizados.getAltura() != null 
                            ? dadosAtualizados.getAltura() 
                            : existente.getAltura());
                    existente.setComprimento(dadosAtualizados.getComprimento() != null 
                            ? dadosAtualizados.getComprimento() 
                            : existente.getComprimento());
                    existente.setComportamento(dadosAtualizados.getComportamento() != null 
                            ? dadosAtualizados.getComportamento() 
                            : existente.getComportamento());
                    Dinossauro atualizado = dinossauroRepository.save(existente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (dinossauroRepository.existsById(id)) {
            dinossauroRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/nome/{nome}")
    @Transactional
    public ResponseEntity<Void> deletarPorNome(@PathVariable String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Dinossauro> encontrados = dinossauroRepository.findByNomeIgnoreCase(nome.trim());
        if (encontrados.isEmpty()) {
            encontrados = dinossauroRepository.findByNomeContainingIgnoreCase(nome.trim());
        }
        if (encontrados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        dinossauroRepository.deleteAll(encontrados);
        return ResponseEntity.noContent().build();
    }
}
