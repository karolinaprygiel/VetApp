package uj.jwzp2021.gp.VetApp.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uj.jwzp2021.gp.VetApp.service.VetService;

@WebMvcTest(VetRestController.class)
class VetRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private VetService vetService;

  @Test
  void getVet() {}

  @Test
  void getAll() {}

  @Test
  void createVet() {}

  @Test
  void deleteVet() {}
}
