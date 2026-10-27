package com.cadastro.dinossauro.controller;

import com.cadastro.dinossauro.model.Dinossauro;
import com.cadastro.dinossauro.repository.DinossauroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/dinossauro", "/dinossauros"})
public class DinossauroController {

    @Autowired
    private DinossauroRepository dinossauroRepository;

    @GetMapping
    public List<Dinossauro> listar() {
        return dinossauroRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dinossauro> buscarPorId(@PathVariable Long id) {
        return dinossauroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dinossauro> cadastrar(@RequestBody Dinossauro dinossauro) {
        Dinossauro salvo = dinossauroRepository.save(dinossauro);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dinossauro> atualizar(@PathVariable Long id, @RequestBody Dinossauro dadosAtualizados) {
        return dinossauroRepository.findById(id)
                .map(existente -> {
                    existente.setNome(dadosAtualizados.getNome());
                    existente.setEspecie(dadosAtualizados.getEspecie());
                    existente.setPeso(dadosAtualizados.getPeso());
                    existente.setAltura(dadosAtualizados.getAltura());
                    existente.setComprimento(dadosAtualizados.getComprimento());
                    existente.setComportamento(dadosAtualizados.getComportamento());
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
}
