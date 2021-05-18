package uj.jwzp2021.gp.VetApp.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uj.jwzp2021.gp.VetApp.service.VisitService;

@WebMvcTest(VisitsRestController.class)
class VisitsRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private VisitService visitService;

  @Test
  void getVisit() {}

  @Test
  void getAllVisits() {}

  @Test
  void createVisit() {}

  @Test
  void delete() {}

  @Test
  void update() {}

  @Test
  void find() {}
}
