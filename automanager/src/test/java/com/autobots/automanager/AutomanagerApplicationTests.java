package com.autobots.automanager;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "automanager.security.username=test-user",
        "automanager.security.password=test-password-123"
})
@AutoConfigureMockMvc
class AutomanagerApplicationTests {

    @Autowired MockMvc mvc;
    @Autowired ClienteRepositorio repositorio;

    @BeforeEach
    void limparBanco() {
        repositorio.deleteAll();
    }

    @Test
    void endpointsRequeremAutenticacao() throws Exception {
        mvc.perform(get("/cliente")).andExpect(status().isUnauthorized());
    }

    @Test
    void realizaCrudCompletoDeCliente() throws Exception {
        String cliente = mvc.perform(post("/cliente")
                        .with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Bruce Wayne\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        String id = com.jayway.jsonpath.JsonPath.read(cliente, "$.id").toString();

        mvc.perform(get("/cliente/" + id).with(httpBasic("test-user", "test-password-123")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Bruce Wayne"));
        mvc.perform(put("/cliente/" + id).with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Bruce T. Wayne\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Bruce T. Wayne"));
        mvc.perform(delete("/cliente/" + id).with(httpBasic("test-user", "test-password-123")))
                .andExpect(status().isNoContent());
        mvc.perform(get("/cliente/" + id).with(httpBasic("test-user", "test-password-123")))
                .andExpect(status().isNotFound());
    }

    @Test
    void realizaCrudDasEntidadesRelacionadas() throws Exception {
        String cliente = mvc.perform(post("/cliente").with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Selina Kyle\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.jayway.jsonpath.JsonPath.read(cliente, "$.id").toString();

        String documento = mvc.perform(post("/cliente/" + id + "/documentos")
                        .with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"tipo\":\"CPF\",\"numero\":\"12345678901\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String documentoId = com.jayway.jsonpath.JsonPath.read(documento, "$.id").toString();
        mvc.perform(get("/cliente/" + id + "/documentos/" + documentoId)
                        .with(httpBasic("test-user", "test-password-123")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.tipo").value("CPF"));
        mvc.perform(put("/cliente/" + id + "/documentos/" + documentoId)
                        .with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"tipo\":\"CPF\",\"numero\":\"12345678902\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.numero").value("12345678902"));
        mvc.perform(delete("/cliente/" + id + "/documentos/" + documentoId)
                        .with(httpBasic("test-user", "test-password-123"))).andExpect(status().isNoContent());

        String telefone = mvc.perform(post("/cliente/" + id + "/telefones").with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"ddd\":\"11\",\"numero\":\"999999999\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String telefoneId = com.jayway.jsonpath.JsonPath.read(telefone, "$.id").toString();
        mvc.perform(get("/cliente/" + id + "/telefones").with(httpBasic("test-user", "test-password-123")))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].numero").value("999999999"));
        mvc.perform(put("/cliente/" + id + "/telefones/" + telefoneId)
                        .with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"ddd\":\"21\",\"numero\":\"888888888\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.ddd").value("21"));
        mvc.perform(delete("/cliente/" + id + "/telefones/" + telefoneId)
                        .with(httpBasic("test-user", "test-password-123"))).andExpect(status().isNoContent());

        mvc.perform(post("/cliente/" + id + "/endereco").with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cidade\":\"Gotham\",\"rua\":\"Rua Wayne\",\"numero\":\"1\"}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/cliente/" + id + "/endereco").with(httpBasic("test-user", "test-password-123")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.cidade").value("Gotham"));
        mvc.perform(put("/cliente/" + id + "/endereco").with(httpBasic("test-user", "test-password-123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cidade\":\"Gotham City\",\"rua\":\"Rua Wayne\",\"numero\":\"2\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.cidade").value("Gotham City"));
        mvc.perform(delete("/cliente/" + id + "/endereco").with(httpBasic("test-user", "test-password-123")))
                .andExpect(status().isNoContent());
    }
}
