package com.cadastro.dinossauro;

import com.cadastro.dinossauro.model.Dinossauro;
import com.cadastro.dinossauro.repository.DinossauroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DinossauroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DinossauroRepository dinossauroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Dinossauro dinoTeste;

    @BeforeEach
    void setUp() {
        dinoTeste = new Dinossauro("Velociraptor Teste", "Velociraptor mongoliensis", 15, 0.5, 2.0, "Carnívoro ágil");
        dinoTeste = dinossauroRepository.save(dinoTeste);
    }

    @Test
    void testListarTodos() throws Exception {
        mockMvc.perform(get("/dinossauro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testBuscarPorIdSucesso() throws Exception {
        mockMvc.perform(get("/dinossauro/" + dinoTeste.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dinoTeste.getId().intValue())))
                .andExpect(jsonPath("$.nome", is("Velociraptor Teste")));
    }

    @Test
    void testBuscarPorIdNaoEncontrado() throws Exception {
        mockMvc.perform(get("/dinossauro/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarPorNomeSucesso() throws Exception {
        mockMvc.perform(get("/dinossauro/nome/Velociraptor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].nome", containsString("Velociraptor")));
    }

    @Test
    void testBuscarPorNomeNaoEncontrado() throws Exception {
        mockMvc.perform(get("/dinossauro/nome/NomeInexistenteXYZ123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarPorQueryParam() throws Exception {
        mockMvc.perform(get("/dinossauro?nome=Velociraptor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testCadastrarSucesso() throws Exception {
        Dinossauro novo = new Dinossauro("Braquiossauro", "Brachiosaurus altithorax", 35000, 13.0, 26.0, "Herbívoro gigante");

        mockMvc.perform(post("/dinossauro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("Braquiossauro")))
                .andExpect(jsonPath("$.dataCriacao", notNullValue()));
    }

    @Test
    void testCadastrarNomeInvalido() throws Exception {
        Dinossauro invalido = new Dinossauro("", "Desconhecido", 100, 1.0, 2.0, "Herbívoro");

        mockMvc.perform(post("/dinossauro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAtualizarSucesso() throws Exception {
        Dinossauro atualizacao = new Dinossauro("Velociraptor Modificado", "Velociraptor", 18, 0.6, 2.1, "Super ágil");

        mockMvc.perform(put("/dinossauro/" + dinoTeste.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Velociraptor Modificado")))
                .andExpect(jsonPath("$.peso", is(18)));
    }

    @Test
    void testAtualizarApenasUmCampo() throws Exception {
        // Atualiza apenas o peso, preservando o nome e as outras características
        String jsonParcial = "{\"peso\": 9999}";

        mockMvc.perform(put("/dinossauro/" + dinoTeste.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParcial))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(dinoTeste.getNome())))
                .andExpect(jsonPath("$.peso", is(9999)))
                .andExpect(jsonPath("$.especie", is(dinoTeste.getEspecie())));
    }

    @Test
    void testAtualizarNaoEncontrado() throws Exception {
        Dinossauro atualizacao = new Dinossauro("Inexistente", "Especie", 100, 1.0, 2.0, "Docil");

        mockMvc.perform(put("/dinossauro/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletarSucesso() throws Exception {
        Dinossauro paraDeletar = dinossauroRepository.save(new Dinossauro("Para Deletar", "Especie", 10, 1.0, 1.0, "Docil"));

        mockMvc.perform(delete("/dinossauro/" + paraDeletar.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/dinossauro/" + paraDeletar.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletarNaoEncontrado() throws Exception {
        mockMvc.perform(delete("/dinossauro/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletarPorNomeSucesso() throws Exception {
        Dinossauro paraDeletar = dinossauroRepository.save(new Dinossauro("Espinossauro Excluir", "Spinosaurus", 7000, 4.0, 14.0, "Carnívoro"));

        mockMvc.perform(delete("/dinossauro/nome/Espinossauro Excluir"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/dinossauro/nome/Espinossauro Excluir"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletarPorNomeNaoEncontrado() throws Exception {
        mockMvc.perform(delete("/dinossauro/nome/InexistenteAbsoluto999"))
                .andExpect(status().isNotFound());
    }
}
