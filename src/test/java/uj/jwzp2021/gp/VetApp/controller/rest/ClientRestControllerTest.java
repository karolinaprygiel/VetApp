package uj.jwzp2021.gp.VetApp.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uj.jwzp2021.gp.VetApp.service.ClientService;

@WebMvcTest(ClientRestController.class)
class ClientRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ClientService clientService;

  @Test
  void getClient() {}

  @Test
  void getAllClients() {}

  @Test
  void createClient() {}

  @Test
  void deleteClient() {}
}
