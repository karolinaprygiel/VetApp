package uj.jwzp2021.gp.VetApp.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.OfficeRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Office;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.service.VetService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VetRestController.class)
class VetRestControllerTest {

  private final String PATH = "/api/vets";

  @Autowired private ObjectMapper mapper;

  @Autowired private MockMvc mockMvc;

  @MockBean private VetService vetService;

  @Test
  void getVet() throws Exception {
    final int ID = 0;
    Vet vet = new Vet(ID, "Vet", "Veterinary", LocalTime.of(12, 30), Duration.ofHours(8));

    given(vetService.getVetById(ID)).willReturn(vet);

    mockMvc
        .perform(get(PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(vet.getId())))
        .andExpect(jsonPath("$.name", is(vet.getName())))
        .andExpect(jsonPath("$.surname", is(vet.getSurname())))
        .andExpect(jsonPath("$.shiftStart").value(is(vet.getShiftStart().toString())))
        .andExpect(jsonPath("$.workingTime").value(is(vet.getWorkingTime().toString())));
  }

  @Test
  void getAll() throws Exception {

    Vet vet1 = new Vet(1, "Vet", "Veterinary", LocalTime.of(12, 30), Duration.ofHours(8));
    Vet vet2 = new Vet(2, "asdfadf", "asdf", LocalTime.of(11, 32), Duration.ofHours(5));
    Vet vet3 = new Vet(3, "asdfasdff", "asdfff", LocalTime.of(3, 43), Duration.ofHours(19));

    given(vetService.getAll()).willReturn(List.of(vet1, vet2, vet3));

    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$", isA(List.class)))
        .andExpect(jsonPath("$[0].id", is(vet1.getId())))
        .andExpect(jsonPath("$[0].name", is(vet1.getName())))
        .andExpect(jsonPath("$[0].surname", is(vet1.getSurname())))
        .andExpect(jsonPath("$[0].shiftStart").value(is(vet1.getShiftStart().toString())))
        .andExpect(jsonPath("$[0].workingTime").value(is(vet1.getWorkingTime().toString())))
        .andExpect(jsonPath("$[1].id", is(vet2.getId())))
        .andExpect(jsonPath("$[1].name", is(vet2.getName())))
        .andExpect(jsonPath("$[1].surname", is(vet2.getSurname())))
        .andExpect(jsonPath("$[1].shiftStart").value(is(vet2.getShiftStart().toString())))
        .andExpect(jsonPath("$[1].workingTime").value(is(vet2.getWorkingTime().toString())))
        .andExpect(jsonPath("$[2].id", is(vet3.getId())))
        .andExpect(jsonPath("$[2].name", is(vet3.getName())))
        .andExpect(jsonPath("$[2].surname", is(vet3.getSurname())))
        .andExpect(jsonPath("$[2].shiftStart").value(is(vet3.getShiftStart().toString())))
        .andExpect(jsonPath("$[2].workingTime").value(is(vet3.getWorkingTime().toString())));

  }

  @Test
  void createVet()  throws Exception {

    Vet vet = new Vet(1, "Vet", "Veterinary", LocalTime.of(12, 30), Duration.ofHours(2));
    VetRequestDto req = new VetRequestDto("Vet", "Veterinary", "12:30", Duration.ofHours(2));

    given(vetService.createVet(req)).willReturn(vet);

    mockMvc
        .perform(
            post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json")
                .content(mapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(vet.getId())))
        .andExpect(jsonPath("$.name", is(req.getName())))
        .andExpect(jsonPath("$.surname", is(req.getSurname())))
        .andExpect(jsonPath("$.shiftStart", is(req.getShiftStart())))
        .andExpect(jsonPath("$.workingTime").value(is(req.getWorkingTime().toString())));
  }

  @Test
  void deleteVet() {}
}
