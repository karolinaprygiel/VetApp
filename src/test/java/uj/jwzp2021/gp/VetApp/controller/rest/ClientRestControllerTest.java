package uj.jwzp2021.gp.VetApp.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.service.ClientService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientRestController.class)
class ClientRestControllerTest {

  private final String PATH = "/api/clients";

  @Autowired private ObjectMapper mapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private ClientService clientService;

  @Test
  void getClient() throws Exception {
    final int ID = 0;
    Client jola = new Client(ID, "Jola", "Jola");
    given(clientService.getClientById(ID)).willReturn(jola);

    mockMvc
        .perform(get(PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(jola.getId())))
        .andExpect(jsonPath("$.name", is(jola.getName())))
        .andExpect(jsonPath("$.surname", is(jola.getSurname())));
  }

  @Test
  void getAllClients() throws Exception {

    Client jola = new Client(0, "Jola", "Jola");
    Client ola = new Client(5, "Ola", "Ola");

    given(clientService.getAll()).willReturn(List.of(jola, ola));
    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$", isA(List.class)))
        .andExpect(jsonPath("$[0].id", is(jola.getId())))
        .andExpect(jsonPath("$[0].name", is(jola.getName())))
        .andExpect(jsonPath("$[0].surname", is(jola.getSurname())))
        .andExpect(jsonPath("$[1].id", is(ola.getId())))
        .andExpect(jsonPath("$[1].name", is(ola.getName())))
        .andExpect(jsonPath("$[1].surname", is(ola.getSurname())));
  }

  @Test
  void createClient() throws Exception {
    Client client = new Client(3, "Władek", "Oak");
    ClientRequestDto crd = new ClientRequestDto("Władek", "Oak");
    given(clientService.createClient(crd)).willReturn(client);

    mockMvc
        .perform(
            post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json")
                .content(mapper.writeValueAsString(crd)))
        .andExpect(status().isCreated())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.name", is(client.getName())))
        .andExpect(jsonPath("$.surname", is(client.getSurname())));
  }

  @Test
  void deleteClient() throws Exception {
    final int ID = 3;
    Client client = new Client(ID, "Władek", "Oak");
    given(clientService.deleteClient(ID)).willReturn(client);

    mockMvc
        .perform(delete(PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.name", is(client.getName())))
        .andExpect(jsonPath("$.surname", is(client.getSurname())));
  }
}
