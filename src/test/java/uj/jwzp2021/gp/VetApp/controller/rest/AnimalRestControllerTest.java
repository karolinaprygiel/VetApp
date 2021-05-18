package uj.jwzp2021.gp.VetApp.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.service.AnimalService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalRestController.class)
class AnimalRestControllerTest {

  private final String PATH = "/api/animals";

  @Autowired private MockMvc mockMvc;

  @MockBean private AnimalService animalService;

  @Test
  void getAnimal() {}

  @Test
  void getAllAnimals() throws Exception {

    Client jola = new Client(0, "Jola", "Jola");

    Animal animal1 = new Animal(0, AnimalType.HAMSTER, "Klemens", 2019, jola);
    Animal animal2 = new Animal(1, AnimalType.DOG, "Pieseł", 2010, new Client());
    Animal animal3 = new Animal(2, AnimalType.CAT, "NyanCat", 2014, new Client());

    given(animalService.getAllAnimals()).willReturn(List.of(animal1, animal2, animal3));

    mockMvc
        .perform(get(PATH).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$", isA(List.class)))
        .andExpect(jsonPath("$[0].name", is("Klemens")))
        .andExpect(jsonPath("$[0].ownerId", is(0)))
        .andExpect(jsonPath("$[1].name", is("Pieseł")))
        .andExpect(jsonPath("$[2].name", is("NyanCat")));
  }

  @Test
  void createAnimal() {}

  @Test
  void deleteAnimal() {}
}
