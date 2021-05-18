package uj.jwzp2021.gp.VetApp.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uj.jwzp2021.gp.VetApp.service.OfficeService;

@WebMvcTest(OfficeRestController.class)
class OfficeRestControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private OfficeService officeService;

  @Test
  void getOffice() {}

  @Test
  void getAllOffices() {}

  @Test
  void createOffice() {}

  @Test
  void deleteOffice() {}
}
