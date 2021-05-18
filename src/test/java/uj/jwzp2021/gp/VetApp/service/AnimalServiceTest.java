package uj.jwzp2021.gp.VetApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.client.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.AnimalMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.Animal;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.AnimalRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

  @InjectMocks
  private AnimalService animalService;
  @Mock
  private AnimalRepository animalRepository;
  @Mock
  private ClientService clientService;
  @Mock
  private AnimalMapper animalMapper;

  private Client client;
  private Animal animal;
  private Animal animal2;

  @BeforeEach
   public void setUp(){
    client = new Client(1, "Jan", "Kowalski");
    animal = new Animal(1, AnimalType.DOG, "Burek", 2010, client);
    animal2 = new Animal(2, AnimalType.CAT, "Mruczek", 2019, client);

  }

  @Test
  void getAnimalById_ValidId_Returns_Animal() {
    given(animalRepository.findById(1)).willReturn(java.util.Optional.of(animal));
    assertThat(animalService.getAnimalById(1)).isNotNull().isEqualTo(animal);
  }

  @Test
  void getAnimalById_InvalidId_Throws_AnimalNotFoundException(){
    given(animalRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(AnimalNotFoundException.class, () -> animalService.getAnimalById(1));
    assertEquals("Animal with id=1 not found", exception.getMessage());
  }
  @Test
  void getAllAnimals_Returns_ListOfAnimals() {
    given(animalRepository.findAll()).willReturn(List.of(animal, animal2));
    assertThat(animalService.getAllAnimals()).isNotNull().hasSize(2).containsExactlyInAnyOrderElementsOf(List.of(animal, animal2));
  }

  @Test
  void getAllAnimals_Returns_ListOfOneAnimal() {
    given(animalRepository.findAll()).willReturn(List.of(animal));
    assertThat(animalService.getAllAnimals()).isNotNull().hasSize(1).containsExactlyInAnyOrderElementsOf(List.of(animal));
  }

  @Test
  void getAllAnimals_Returns_EmptyList(){
    given(animalRepository.findAll()).willReturn(Collections.emptyList());
    assertThat(animalService.getAllAnimals()).isEmpty();
  }

  @Test
  void deleteAnimal_AnimalExists_DeletesAndReturns_Animal() {
    given(animalRepository.findById(1)).willReturn(Optional.of(animal));
    assertThat(animalService.deleteAnimal(1)).isEqualTo(animal);
    verify(animalRepository, Mockito.times(1)).delete(animal);
    verify(animalRepository, Mockito.times(1)).findById(1);
  }

  @Test
  void deleteAnimal_AnimalNotExists_Throws_AnimalNotFoundException() {
    given(animalRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(AnimalNotFoundException.class, () -> animalService.getAnimalById(1));
    assertEquals("Animal with id=1 not found", exception.getMessage());
    verifyNoMoreInteractions(animalRepository);
  }

  @Test
  void createAnimal_ownerExists_SaveAndReturns_Animal() {
    AnimalRequestDto animalReq = new AnimalRequestDto(AnimalType.DOG, "Burek", 2010, 1);
    given(animalMapper.toAnimal(animalReq, client)).willReturn(animal);
    given(clientService.getClientById(1)).willReturn(client);
    given(animalRepository.save(animal)).willReturn(animal);

    assertThat(animalService.createAnimal(animalReq)).isNotNull().isEqualTo(animal);
    verify(animalRepository, Mockito.times(1)).save(animal);
  }

  @Test
  void createAnimal_ownerNotExists_Throw_ClientNotFoundException() {
    AnimalRequestDto animalReq = new AnimalRequestDto(AnimalType.DOG, "Burek", 2010, 1);
    given(clientService.getClientById(1))
        .willThrow(new ClientNotFoundException("Client with id: 1  not found"));

    VeterinaryAppException exception =
        assertThrows(ClientNotFoundException.class, () -> animalService.createAnimal(animalReq));
    assertEquals("Client with id: 1  not found", exception.getMessage());
    verify(animalRepository, Mockito.times(0)).save(any());
  }


}