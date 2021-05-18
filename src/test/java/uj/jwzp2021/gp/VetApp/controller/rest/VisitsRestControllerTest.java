package uj.jwzp2021.gp.VetApp.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Responses.VisitDatesResponseDto;
import uj.jwzp2021.gp.VetApp.model.entity.*;
import uj.jwzp2021.gp.VetApp.service.VisitService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VisitsRestController.class)
class VisitsRestControllerTest {

  private final String PATH = "/api/visits";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @MockBean private VisitService visitService;

  @Test
  void getVisit() throws Exception {
    final int ID = 3;
    Vet vet = new Vet(34, "Vet", "Vet", null, null);
    Client wladek = new Client(3, "Władek", "Oak");
    Animal fafik = new Animal(45, AnimalType.DOG, "Fafik", 1985, wladek);
    Office office = new Office(1, "Office");
    Visit visit =
        new Visit(
            ID,
            LocalDateTime.of(2020, 11, 12, 12, 12, 50),
            Duration.ofHours(1),
            VisitStatus.PLANNED,
            BigDecimal.valueOf(20.50),
            fafik,
            wladek,
            vet,
            office,
            "");
    given(visitService.getVisitById(ID)).willReturn(visit);

    mockMvc
        .perform(get(PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(ID)))
        .andExpect(jsonPath("$.animalId", is(visit.getAnimal().getId())))
        .andExpect(jsonPath("$.clientId", is(visit.getClient().getId())))
        .andExpect(jsonPath("$.vetId", is(visit.getVet().getId())))
        .andExpect(jsonPath("$.startTime", is(visit.getStartTime().toString())))
        .andExpect(jsonPath("$.duration", is(visit.getDuration().toString())))
        .andExpect(jsonPath("$.status", is(visit.getVisitStatus().toString())))
        .andExpect(jsonPath("$.price").value(visit.getPrice()));
  }

  @Test
  void getAllVisits() throws Exception {
    final int ID1 = 3;
    final int ID2 = 4;
    Vet vet = new Vet(34, "Vet", "Vet", null, null);
    Client wladek = new Client(3, "Władek", "Oak");
    Animal fafik = new Animal(45, AnimalType.DOG, "Fafik", 1985, wladek);
    Office office = new Office(1, "Office");
    Visit visit1 =
        new Visit(
            ID1,
            LocalDateTime.of(2020, 11, 12, 12, 12, 50),
            Duration.ofHours(1),
            VisitStatus.PLANNED,
            BigDecimal.valueOf(20),
            fafik,
            wladek,
            vet,
            office,
            "");
    Visit visit2 =
        new Visit(
            ID2,
            LocalDateTime.of(2022, 11, 12, 12, 12, 50),
            Duration.ofHours(1),
            VisitStatus.PLANNED,
            BigDecimal.valueOf(20),
            fafik,
            wladek,
            vet,
            office,
            "");
    given(visitService.getAll()).willReturn(List.of(visit1, visit2));

    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$[0].id", is(ID1)))
        .andExpect(jsonPath("$[0].animalId", is(visit1.getAnimal().getId())))
        .andExpect(jsonPath("$[0].clientId", is(visit1.getClient().getId())))
        .andExpect(jsonPath("$[0].vetId", is(visit1.getVet().getId())))
        .andExpect(jsonPath("$[0].startTime", is(visit1.getStartTime().toString())))
        .andExpect(jsonPath("$[1].id", is(ID2)))
        .andExpect(jsonPath("$[1].animalId", is(visit2.getAnimal().getId())))
        .andExpect(jsonPath("$[1].clientId", is(visit2.getClient().getId())))
        .andExpect(jsonPath("$[1].vetId", is(visit2.getVet().getId())))
        .andExpect(jsonPath("$[1].startTime", is(visit2.getStartTime().toString())));
  }

  @Test
  void createVisit() throws Exception {
    final int ID = 3;
    final int ANIMALID = 45;
    final int CLIENTID = 3;
    final int VETID = 34;
    final int OFFICEID = 1;
    final LocalDateTime STARTTIME = LocalDateTime.of(2020, 11, 12, 12, 12, 50);
    final BigDecimal PRICE = BigDecimal.valueOf(20.50);
    final Duration DURATION = Duration.ofHours(1);
    final VisitStatus STATUS = VisitStatus.PLANNED;
    Client wladek = new Client(CLIENTID, "Władek", "Oak");
    Animal fafik = new Animal(ANIMALID, AnimalType.DOG, "Fafik", 1985, wladek);
    Vet vet = new Vet(VETID, "Vet", "Vet", null, null);
    Office office = new Office(OFFICEID, "Office");
    VisitRequestDto req =
        new VisitRequestDto(STARTTIME, DURATION, PRICE, ANIMALID, CLIENTID, VETID, OFFICEID);
    Visit visit = new Visit(ID, STARTTIME, DURATION, STATUS, PRICE, fafik, wladek, vet, office, "");
    given(visitService.createVisit(req)).willReturn(visit);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json")
                .content(mapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(ID)))
        .andExpect(jsonPath("$.animalId", is(visit.getAnimal().getId())))
        .andExpect(jsonPath("$.clientId", is(visit.getClient().getId())))
        .andExpect(jsonPath("$.vetId", is(visit.getVet().getId())))
        .andExpect(jsonPath("$.startTime", is(visit.getStartTime().toString())))
        .andExpect(jsonPath("$.duration", is(visit.getDuration().toString())))
        .andExpect(jsonPath("$.status", is(visit.getVisitStatus().toString())))
        .andExpect(jsonPath("$.price").value(visit.getPrice()));
  }

  @Test
  void delete() throws Exception {
    final int ID = 3;
    Vet vet = new Vet(34, "Vet", "Vet", null, null);
    Client wladek = new Client(3, "Władek", "Oak");
    Animal fafik = new Animal(45, AnimalType.DOG, "Fafik", 1985, wladek);
    Office office = new Office(1, "Office");
    Visit visit =
        new Visit(
            ID,
            LocalDateTime.of(2020, 11, 12, 12, 12, 50),
            Duration.ofHours(1),
            VisitStatus.PLANNED,
            BigDecimal.valueOf(20.50),
            fafik,
            wladek,
            vet,
            office,
            "");
    given(visitService.delete(ID)).willReturn(visit);

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(
                    PATH + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(ID)))
        .andExpect(jsonPath("$.animalId", is(visit.getAnimal().getId())))
        .andExpect(jsonPath("$.clientId", is(visit.getClient().getId())))
        .andExpect(jsonPath("$.vetId", is(visit.getVet().getId())))
        .andExpect(jsonPath("$.startTime", is(visit.getStartTime().toString())))
        .andExpect(jsonPath("$.duration", is(visit.getDuration().toString())))
        .andExpect(jsonPath("$.status", is(visit.getVisitStatus().toString())))
        .andExpect(jsonPath("$.price").value(visit.getPrice()));
  }

  @Test
  void update() throws Exception {
    final int ID = 3;
    final int ANIMALID = 45;
    final int CLIENTID = 3;
    final int VETID = 34;
    final int OFFICEID = 1;
    final LocalDateTime STARTTIME = LocalDateTime.of(2020, 11, 12, 12, 12, 50);
    final BigDecimal PRICE = BigDecimal.valueOf(20.50);
    final Duration DURATION = Duration.ofHours(1);
    final VisitStatus STATUS = VisitStatus.PLANNED;
    final String DESCRIPTION = "description";

    Client client = new Client(CLIENTID, "Władek", "Oak");
    Animal animal = new Animal(ANIMALID, AnimalType.DOG, "Fafik", 1985, client);
    Vet vet = new Vet(VETID, "Vet", "Vet", null, null);
    Office office = new Office(OFFICEID, "Office");
    final VisitUpdateRequestDto REQ = new VisitUpdateRequestDto(STATUS, DESCRIPTION);
    Visit visit =
        new Visit(ID, STARTTIME, DURATION, STATUS, PRICE, animal, client, vet, office, "");

    given(visitService.updateVisit(ID, REQ)).willReturn(visit);

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch(PATH + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json")
                .content(mapper.writeValueAsString(REQ)))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.id", is(ID)))
        .andExpect(jsonPath("$.animalId", is(visit.getAnimal().getId())))
        .andExpect(jsonPath("$.clientId", is(visit.getClient().getId())))
        .andExpect(jsonPath("$.vetId", is(visit.getVet().getId())))
        .andExpect(jsonPath("$.startTime", is(visit.getStartTime().toString())))
        .andExpect(jsonPath("$.duration", is(visit.getDuration().toString())))
        .andExpect(jsonPath("$.status", is(visit.getVisitStatus().toString())))
        .andExpect(jsonPath("$.price").value(visit.getPrice()));
  }

  @Test
  void find() throws Exception {
    final int ID = 3;
    Vet vet = new Vet(34, "Vet", "Vet", null, null);
    Client wladek = new Client(3, "Władek", "Oak");
    Animal fafik = new Animal(45, AnimalType.DOG, "Fafik", 1985, wladek);
    Office office = new Office(1, "Office");
    final LocalDateTime DATE_FROM = LocalDate.parse("2020-01-01").atTime(12, 23, 23);
    final LocalDateTime DATE_TO = LocalDate.parse("2050-01-01").atTime(12, 42, 11);
    final Duration DURATION = Duration.ofMinutes(20);
    final int PREFERRED_VET_ID = 34;
    VisitDatesResponseDto vdrd = new VisitDatesResponseDto(DATE_FROM, DURATION, 45, 1);
    given(visitService.findVisits(DATE_FROM, DATE_TO, DURATION, PREFERRED_VET_ID))
        .willReturn(List.of(vdrd));

    mockMvc
        .perform(
            get(PATH + "/find")
                .queryParam("dateFrom", DATE_FROM.toString())
                .queryParam("dateTo", DATE_TO.toString())
                .queryParam("duration", DURATION.toString())
                .queryParam("preferredVet", "" + PREFERRED_VET_ID)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$[0].startTime").value(is(vdrd.getStartTime().toString())))
        .andExpect(jsonPath("$[0].duration").value(is(vdrd.getDuration().toString())))
        .andExpect(jsonPath("$[0].vetId", is(vdrd.getVetId())))
        .andExpect(jsonPath("$[0].officeId", is(vdrd.getOfficeId())));
  }
}
