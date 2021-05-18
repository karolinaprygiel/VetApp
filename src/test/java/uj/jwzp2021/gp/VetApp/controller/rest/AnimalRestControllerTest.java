package uj.jwzp2021.gp.VetApp.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalRestController.class)
class AnimalRestControllerTest {

  private final String PATH = "/api/animals";

  @Autowired private MockMvc mockMvc;

  @MockBean private AnimalService animalService;

  @Autowired private ObjectMapper mapper;

  @Test
  void getAnimal() throws Exception {
    final int ID = 0;
    Client jola = new Client(0, "Jola", "Jola");
    Animal animal = new Animal(ID, AnimalType.HAMSTER, "Klemens", 2019, jola);
    given(animalService.getAnimalById(ID)).willReturn(animal);

    mockMvc
        .perform(get(PATH + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.name", is(animal.getName())))
        .andExpect(jsonPath("$.animalType", is(animal.getType().toString())))
        .andExpect(jsonPath("$.yearOfBirth", is(animal.getYearOfBirth())))
        .andExpect(jsonPath("$.ownerId", is(animal.getOwner().getId())));
  }

  @Test
  void getAllAnimals() throws Exception {

    Client jola = new Client(0, "Jola", "Jola");
    Client ola = new Client(5, "Ola", "Ola");

    Animal animal0 = new Animal(0, AnimalType.HAMSTER, "Klemens", 2019, jola);
    Animal animal1 = new Animal(1, AnimalType.DOG, "Pieseł", 2010, ola);
    Animal animal2 = new Animal(2, AnimalType.CAT, "NyanCat", 2014, new Client());

    given(animalService.getAllAnimals()).willReturn(List.of(animal0, animal1, animal2));

    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$", isA(List.class)))
        .andExpect(jsonPath("$[0].name", is(animal0.getName())))
        .andExpect(jsonPath("$[0].animalType", is(animal0.getType().toString())))
        .andExpect(jsonPath("$[0].ownerId", is(animal0.getOwner().getId())))
        .andExpect(jsonPath("$[1].name", is(animal1.getName())))
        .andExpect(jsonPath("$[1].animalType", is(animal1.getType().toString())))
        .andExpect(jsonPath("$[1].ownerId", is(animal1.getOwner().getId())))
        .andExpect(jsonPath("$[2].name", is(animal2.getName())))
        .andExpect(jsonPath("$[2].animalType", is(animal2.getType().toString())))
        .andExpect(jsonPath("$[2].ownerId", is(animal2.getOwner().getId())));
  }

  @Test
  void createAnimal() throws Exception {
    Client wladek = new Client(3, "Władek", "Oak");

    AnimalRequestDto fafReq = new AnimalRequestDto(AnimalType.DOG, "Fafik", 1985, 3);

    Animal fafik = new Animal(45, AnimalType.DOG, "Fafik", 1985, wladek);

    given(animalService.createAnimal(fafReq)).willReturn(fafik);

    mockMvc
        .perform(
            post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json")
                .content(mapper.writeValueAsString(fafReq)))
        .andExpect(status().isCreated())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.name", is(fafReq.getName())))
        .andExpect(jsonPath("$.animalType", is(fafReq.getType().toString())))
        .andExpect(jsonPath("$.yearOfBirth", is(fafReq.getYearOfBirth())))
        .andExpect(jsonPath("$.ownerId", is(fafReq.getOwnerId())));
  }

  @Test
  void deleteAnimal() throws Exception {
    final int FAFID = 45;
    Client wladek = new Client(3, "Władek", "Oak");
    AnimalRequestDto fafReq = new AnimalRequestDto(AnimalType.DOG, "Fafik", 1985, 3);
    Animal fafik = new Animal(FAFID, AnimalType.DOG, "Fafik", 1985, wladek);
    given(animalService.deleteAnimal(FAFID)).willReturn(fafik);

    mockMvc
        .perform(delete(PATH + "/" + FAFID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.name", is(fafReq.getName())))
        .andExpect(jsonPath("$.animalType", is(fafReq.getType().toString())))
        .andExpect(jsonPath("$.yearOfBirth", is(fafReq.getYearOfBirth())))
        .andExpect(jsonPath("$.ownerId", is(fafReq.getOwnerId())));
  }
}
