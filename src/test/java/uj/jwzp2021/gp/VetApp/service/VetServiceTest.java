
package uj.jwzp2021.gp.VetApp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.VetMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VetRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;
import uj.jwzp2021.gp.VetApp.model.entity.Vet;
import uj.jwzp2021.gp.VetApp.repository.VetRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetServiceTest {

  @InjectMocks
  private VetService vetService;
  @Mock
  private VetRepository vetRepository;
  @Mock
  private VetMapper vetMapper;

  private static Vet vet;
  private static Vet vet2;

  @BeforeAll
  static void setUp(){
    vet = new Vet(1, "Piotr", "Weterynaryjny", LocalTime.parse("09:00"), Duration.ofHours(9));
    vet2 = new Vet(1, "Andrzej", "Lekarski", LocalTime.parse("16:00"), Duration.ofHours(10));
  }

  @Test
  void getVetById_ValidId_Returns_Vet() {
    given(vetRepository.findById(1)).willReturn(Optional.of(vet));
    assertThat(vetService.getVetById(1)).isNotNull().isEqualTo(vet);
  }

  @Test
  void getVetById_invalidId_Throws_VetNotFoundException() {
    given(vetRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(VetNotFoundException.class, () -> vetService.getVetById(1));
    assertEquals("Vet with id=1 not found", exception.getMessage());
  }

  @Test
  void getAll_Returns_ListOfVets() {
    given(vetRepository.findAll()).willReturn(List.of(vet2, vet));
    assertThat(vetService.getAll()).isNotNull().hasSize(2).containsExactlyInAnyOrderElementsOf(List.of(vet, vet2));
  }

  @Test
  void getAll_Returns_ListOfOneVet() {
    given(vetRepository.findAll()).willReturn(List.of(vet));
    assertThat(vetService.getAll()).isNotNull().hasSize(1).containsExactlyInAnyOrderElementsOf(List.of(vet));
  }

  @Test
  void getAll_Returns_EmptyList() {
    given(vetRepository.findAll()).willReturn(Collections.emptyList());
    assertThat(vetService.getAll()).isNotNull().isEmpty();
  }

  @Test
  void deleteVet_VetExists_DeleteAndReturns_Vet() {
    given(vetRepository.findById(1)).willReturn(Optional.of(vet));
    assertThat(vetService.deleteVet(1)).isEqualTo(vet);
    verify(vetRepository, Mockito.times(1)).delete(vet);
    verify(vetRepository, Mockito.times(1)).findById(1);
  }

  @Test
  void deleteVet_VetNotExists_Throws_VetNotFoundException() {
    given(vetRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(VetNotFoundException.class, () -> vetService.getVetById(1));
    assertEquals("Vet with id=1 not found", exception.getMessage());
    verifyNoMoreInteractions(vetRepository);
  }


  void createVet_ReturnsAndSave_Animal() {
    VetRequestDto vetReq = new VetRequestDto("Piotr", "Weterynaryjny", "09:00", Duration.ofHours(9));
    given(vetMapper.toVet(vetReq)).willReturn(vet);
    given(vetRepository.save(vet)).willReturn(vet);

    assertThat(vetService.createVet(vetReq)).isNotNull().isEqualTo(vet);
    verify(vetRepository, Mockito.times(1)).save(vet);
  }

  @Test
  void worksAtMidnight_Returns_False() {
    assertFalse(vetService.worksAtMidnight(vet));
  }

  @Test
  void worksAtMidnight_Returns_True() {
    assertTrue(vetService.worksAtMidnight(vet2));
  }

  @ParameterizedTest
  @MethodSource("isVetAtWorkDataProvider")
  void isVetAtWork(LocalDateTime startTime, Duration duration, Vet vet, Boolean result) {
    assertThat(vetService.isVetAtWork(startTime, duration, vet)).isEqualTo(result);

  }

  private static Stream<Arguments> isVetAtWorkDataProvider(){
    return Stream.of(
        Arguments.of(LocalDateTime.of(2021, 4, 15, 20, 0),
            Duration.ofMinutes(20), vet2, true),
        Arguments.of(LocalDateTime.of(2021, 4, 15, 23, 55),
            Duration.ofMinutes(15), vet2, true),
        Arguments.of(LocalDateTime.of(2021, 4, 16, 1, 10),
            Duration.ofMinutes(12), vet2, true),
        Arguments.of(LocalDateTime.of(2021, 4, 16, 8, 10),
            Duration.ofMinutes(15), vet2, false),
        Arguments.of(LocalDateTime.of(2021, 4, 16, 4, 10),
            Duration.ofMinutes(15), vet2, false),
        Arguments.of(LocalDateTime.of(2021, 5, 15, 15, 55),
            Duration.ofMinutes(12), vet2, false),
        Arguments.of(LocalDateTime.of(2021, 7, 20, 1, 55),
            Duration.ofMinutes(12), vet2, false),
        Arguments.of(LocalDateTime.of(2021, 1, 15, 1, 45),
            Duration.ofMinutes(15), vet2, true),
        Arguments.of(LocalDateTime.of(2021, 4, 15, 10, 45),
            Duration.ofMinutes(15), vet, true),
        Arguments.of(LocalDateTime.of(2021, 4, 15, 9, 0),
            Duration.ofMinutes(15), vet, true),
        Arguments.of(LocalDateTime.of(2021, 4, 15, 4, 0),
            Duration.ofMinutes(15), vet, false),
        Arguments.of(LocalDateTime.of(2021, 3, 15, 21, 0),
            Duration.ofMinutes(15), vet, false),
        Arguments.of(LocalDateTime.of(2021, 4, 15, 23, 55),
            Duration.ofMinutes(15), vet, false),
        Arguments.of(LocalDateTime.of(2020, 12, 31, 23, 55),
            Duration.ofMinutes(15), vet2, true)
    );
  }
}