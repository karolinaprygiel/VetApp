package uj.jwzp2021.gp.VetApp.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.service.OfficeService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfficeRestController.class)
class OfficeRestControllerTest {

  private final String PATH = "/api/offices";

  @Autowired private ObjectMapper mapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private OfficeService officeService;

  @Test
  void getOffice() throws Exception {
    final int ID = 0;
    Office office = new Office(ID, "high5");

    given(officeService.getOfficeById(ID)).willReturn(office);

    mockMvc
        .perform(get(PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(office.getId())))
        .andExpect(jsonPath("$.name", is(office.getName())));
  }

  @Test
  void getAllOffices() throws Exception {

    Office o1 = new Office(1, "high5");
    Office o2 = new Office(2, "Astris");
    Office o3 = new Office(3, "ofis");

    given(officeService.getAll()).willReturn(List.of(o1, o2, o3));

    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$", isA(List.class)))
        .andExpect(jsonPath("$[0].id", is(o1.getId())))
        .andExpect(jsonPath("$[0].name", is(o1.getName())))
        .andExpect(jsonPath("$[1].id", is(o2.getId())))
        .andExpect(jsonPath("$[1].name", is(o2.getName())))
        .andExpect(jsonPath("$[2].id", is(o3.getId())))
        .andExpect(jsonPath("$[2].name", is(o3.getName())));
  }

  @Test
  void createOffice() throws Exception {

    Office office = new Office(1, "high5");
    OfficeRequestDto req = new OfficeRequestDto("high5");

    given(officeService.createOffice(req)).willReturn(office);

    mockMvc
        .perform(
            post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json")
                .content(mapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.name", is(req.getName())));
  }

  @Test
  void deleteOffice() throws Exception {
    final int ID = 45;
    Office office = new Office(ID, "high5");

    given(officeService.deleteOffice(ID)).willReturn(office);

    mockMvc
        .perform(delete(PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(office.getId())))
        .andExpect(jsonPath("$.name", is(office.getName())));
  }
}
