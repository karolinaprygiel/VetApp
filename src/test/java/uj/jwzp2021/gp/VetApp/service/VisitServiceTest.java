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
import uj.jwzp2021.gp.VetApp.exception.office.OfficeNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.vet.VetNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.visit.VisitNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.visit.VisitStartsInPastException;
import uj.jwzp2021.gp.VetApp.exception.visit.VisitTooSoonException;
import uj.jwzp2021.gp.VetApp.exception.visit.WrongOwnerException;
import uj.jwzp2021.gp.VetApp.mapper.VisitMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.VisitUpdateRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.*;
import uj.jwzp2021.gp.VetApp.repository.VisitRepository;

import java.math.BigDecimal;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

  @InjectMocks private VisitService visitService;
  @Mock private VisitRepository visitRepository;
  @Mock private AnimalService animalService;
  @Mock private ClientService clientService;
  @Mock private VetService vetService;
  @Mock private OfficeService officeService;
  @Mock private VisitMapper visitMapper;
  @Mock private Clock clock;
  @Mock private VisitRequestDto mockVisitReq;
  @Mock private Animal mockAnimal;
  @Mock private Client mockClient;
  @Mock private Vet mockVet;
  @Mock private Office mockOffice;
  @Mock private Visit mockVisit;
  private Clock fixedClock;
  private Visit visit;
  private VisitRequestDto visitReq;
  private Animal animal;
  private Client client;
  private Vet vet;
  private Office office;

  @BeforeEach
  void setUp() {
    client = new Client(3, "ClientName", "ClientSurname");
    animal = new Animal(2, AnimalType.RAT, "Rafałek", 2020, client);
    vet = new Vet(4, "Fafał", "Kawa", LocalTime.parse("09:00"), Duration.ofHours(9));
    office = new Office(5, "gab1");

    visit =
        new Visit(
            1,
            LocalDateTime.of(2021, 7, 21, 15, 20),
            Duration.ofMinutes(15),
            VisitStatus.PLANNED,
            BigDecimal.valueOf(50),
            animal,
            client,
            vet,
            office,
            "SomeDescriptiom");
    visitReq =
        new VisitRequestDto(
            LocalDateTime.of(2021, 7, 21, 15, 20),
            Duration.ofMinutes(15),
            BigDecimal.valueOf(50),
            2,
            3,
            4,
            5);
  }

  @Test
  void getVisitById_ValidId_Returns_Visit() {
    given(visitRepository.findById(1)).willReturn(Optional.of(visit));
    assertThat(visitService.getVisitById(1)).isNotNull().isEqualTo(visit);
  }

  @Test
  void getVisitById_invalidId_Throws_VisitNotFoundException() {
    given(visitRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception =
        assertThrows(VisitNotFoundException.class, () -> visitService.getVisitById(1));
    assertEquals("Visit with id=1 not found", exception.getMessage());
  }

  @Test
  void getAll_Returns_ListOfVisits() {
    Visit visit2 =
        new Visit(
            2,
            LocalDateTime.of(2018, 11, 20, 12, 0),
            Duration.ofMinutes(10),
            VisitStatus.FINISHED,
            BigDecimal.valueOf(10),
            null,
            null,
            null,
            null,
            "Another description");
    given(visitRepository.findAll()).willReturn(List.of(visit2, visit));
    assertThat(visitService.getAll())
        .isNotNull()
        .hasSize(2)
        .containsExactlyInAnyOrderElementsOf(List.of(visit, visit2));
  }

  @Test
  void getAll_Returns_ListOfOneVisit() {
    given(visitRepository.findAll()).willReturn(List.of(visit));
    assertThat(visitService.getAll())
        .isNotNull()
        .hasSize(1)
        .containsExactlyInAnyOrderElementsOf(List.of(visit));
  }

  @Test
  void getAll_Returns_EmptyList() {
    given(visitRepository.findAll()).willReturn(Collections.emptyList());
    assertThat(visitService.getAll()).isNotNull().isEmpty();
  }

  @Test
  void deleteVisit_VisitExists_DeleteAndReturns_Visit() {
    given(visitRepository.findById(1)).willReturn(Optional.of(visit));
    assertThat(visitService.delete(1)).isEqualTo(visit);
    verify(visitRepository, Mockito.times(1)).delete(visit);
    verify(visitRepository, Mockito.times(1)).findById(1);
  }

  @Test
  void deleteVisit_VisitNotExists_Throws_VisitNotFoundException() {
    given(visitRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception =
        assertThrows(VisitNotFoundException.class, () -> visitService.getVisitById(1));
    assertEquals("Visit with id=1 not found", exception.getMessage());
    verifyNoMoreInteractions(visitRepository);
  }

  @Test
  void createVisit_ClientNotExists_Throws_ClientNotFoundException() {
    given(clientService.getClientById(3))
        .willThrow(new ClientNotFoundException("Client with id: 1  not found"));
    VeterinaryAppException exception =
        assertThrows(ClientNotFoundException.class, () -> visitService.createVisit(visitReq));
    assertEquals("Client with id: 1  not found", exception.getMessage());
    verifyNoInteractions(visitRepository);
  }

  @Test
  void createVisit_AnimalNotExists_Throws_AnimalNotFoundException() {
    given(animalService.getAnimalById(2))
        .willThrow(new AnimalNotFoundException("Animal with id: 2  not found"));
    VeterinaryAppException exception =
        assertThrows(AnimalNotFoundException.class, () -> visitService.createVisit(visitReq));
    assertEquals("Animal with id: 2  not found", exception.getMessage());
    verifyNoInteractions(visitRepository);
  }

  @Test
  void createVisit_VetNotExists_Throws_VetNotFoundException() {
    given(vetService.getVetById(4)).willThrow(new VetNotFoundException("Vet with id=4 not found"));
    VeterinaryAppException exception =
        assertThrows(VetNotFoundException.class, () -> visitService.createVisit(visitReq));
    assertEquals("Vet with id=4 not found", exception.getMessage());
    verifyNoInteractions(visitRepository);
  }

  @Test
  void createVisit_OfficeNotExists_Throws_OfficeNotFoundException() {
    final int ID = 5;
    given(officeService.getOfficeById(ID))
        .willThrow(new OfficeNotFoundException("Office with id=" + ID + " not found"));
    VeterinaryAppException exception =
        assertThrows(OfficeNotFoundException.class, () -> visitService.createVisit(visitReq));
    assertEquals("Office with id=" + ID + " not found", exception.getMessage());
    verifyNoInteractions(visitRepository);
  }

  @Test
  void createVisit_DateInPast_Throws_VisitStartsInPastException() {
    final int MOCK_ANIMAL_ID = 1234;
    final int MOCK_CLIENT_ID = 6789;
    final int MOCK_VET_ID = 123423;
    final int MOCK_OFFICE_ID = 671289;
        given(mockAnimal.getOwner()).willReturn(mockClient);

    given(mockClient.getId()).willReturn(MOCK_CLIENT_ID);

    /* Services */
    given(animalService.getAnimalById(MOCK_ANIMAL_ID)).willReturn(mockAnimal);
    given(clientService.getClientById(MOCK_CLIENT_ID)).willReturn(mockClient);
    given(vetService.getVetById(MOCK_VET_ID)).willReturn(mockVet);
    given(officeService.getOfficeById(MOCK_OFFICE_ID)).willReturn(mockOffice);

    /* Visit Request */
    given(mockVisitReq.getAnimalId()).willReturn(MOCK_ANIMAL_ID);
    given(mockVisitReq.getClientId()).willReturn(MOCK_CLIENT_ID);
    given(mockVisitReq.getVetId()).willReturn(MOCK_VET_ID);
    given(mockVisitReq.getOfficeId()).willReturn(MOCK_OFFICE_ID);

//    given(animalService.getAnimalById(visitReq.getAnimalId())).willReturn(animal);
//    given(clientService.getClientById(visitReq.getClientId())).willReturn(client);
//    given(vetService.getVetById(visitReq.getVetId())).willReturn(vet);
//    given(officeService.getOfficeById(visitReq.getOfficeId())).willReturn(office);
    fixedClock =
        Clock.fixed(
            Instant.from(LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)),
            ZoneId.systemDefault());
    doReturn(fixedClock.instant()).when(clock).instant();
    doReturn(fixedClock.getZone()).when(clock).getZone();

    given(mockVisitReq.getStartTime()).willReturn(LocalDateTime.of(2010, 1, 1, 1, 1));
    VeterinaryAppException exception =
        assertThrows(
            VisitStartsInPastException.class, () -> visitService.createVisit(mockVisitReq));
    assertEquals("Visit can not start in the past.", exception.getMessage());
    verifyNoInteractions(visitRepository);
  }

  @Test
  void createVisit_DateToSoon_Throws_VisitTooSoonException() {
    final int MOCK_ANIMAL_ID = 1234;
    final int MOCK_CLIENT_ID = 6789;
    final int MOCK_VET_ID = 123423;
    final int MOCK_OFFICE_ID = 671289;
    given(mockAnimal.getOwner()).willReturn(mockClient);

    given(mockClient.getId()).willReturn(MOCK_CLIENT_ID);

    /* Services */
    given(animalService.getAnimalById(MOCK_ANIMAL_ID)).willReturn(mockAnimal);
    given(clientService.getClientById(MOCK_CLIENT_ID)).willReturn(mockClient);
    given(vetService.getVetById(MOCK_VET_ID)).willReturn(mockVet);
    given(officeService.getOfficeById(MOCK_OFFICE_ID)).willReturn(mockOffice);

    /* Visit Request */
    given(mockVisitReq.getAnimalId()).willReturn(MOCK_ANIMAL_ID);
    given(mockVisitReq.getClientId()).willReturn(MOCK_CLIENT_ID);
    given(mockVisitReq.getVetId()).willReturn(MOCK_VET_ID);
    given(mockVisitReq.getOfficeId()).willReturn(MOCK_OFFICE_ID);

    fixedClock =
        Clock.fixed(
            Instant.from(LocalDateTime.of(2020, 1, 1, 2, 0).toInstant(ZoneOffset.UTC)),
            ZoneId.of("UTC"));
    doReturn(fixedClock.instant()).when(clock).instant();
    doReturn(fixedClock.getZone()).when(clock).getZone();


    given(mockVisitReq.getStartTime()).willReturn(LocalDateTime.of(2020, 1, 1, 2, 15));
    VeterinaryAppException exception =
        assertThrows(
            VisitTooSoonException.class, () -> visitService.createVisit(mockVisitReq));
    assertEquals("Visit cannot be scheduled for less an hour from now on.", exception.getMessage());
    verifyNoInteractions(visitRepository);
  }

  @Test
  void createVisit_vetNotAvailable_Throws_VetNotAvailableException() {

  }

  @Test
  void createVisit_dateNotAvailable_Throws_VisitOverlapsException() {}

  @Test
  void createVisit_WrongOwner_Throws_WrongOwnerException() {
    VisitRequestDto anotherVisitReq = new VisitRequestDto(
        LocalDateTime.of(2021, 7, 21, 15, 20),
        Duration.ofMinutes(15),
        BigDecimal.valueOf(50),
        2,
        15,
        4,
        5);
    Client anotherClient = new Client(15, "wrongOwner", "wrongOwnerSurname" );
    given(clientService.getClientById(anotherVisitReq.getClientId())).willReturn(anotherClient);
    given(animalService.getAnimalById(anotherVisitReq.getAnimalId())).willReturn(animal);
    given(vetService.getVetById(anotherVisitReq.getVetId())).willReturn(vet);
    given(officeService.getOfficeById(anotherVisitReq.getOfficeId())).willReturn(office);

    VeterinaryAppException exception =
        assertThrows(
            WrongOwnerException.class, () -> visitService.createVisit(anotherVisitReq));
    assertEquals("This person does not own this animal.", exception.getMessage());
    verifyNoInteractions(visitRepository);
  }

  @Test
  void createVisit_ReturnsAndSave_Visit() {
    fixedClock =
        Clock.fixed(
            Instant.from(LocalDateTime.of(2020, 1, 1, 1, 1).toInstant(ZoneOffset.UTC)),
            ZoneId.systemDefault());
    doReturn(fixedClock.instant()).when(clock).instant();
    doReturn(fixedClock.getZone()).when(clock).getZone();
    given(vetService.isVetAtWork(any(),any(),any())).willReturn(true);

    assertThat(visitService.createVisit(visitReq)).isNotNull().isEqualTo(visit);
    verify(visitRepository, Mockito.times(1)).save(mockVisit);
  }

  @Test
  void updateVisit_VisitNotExists_Throws_VisitNotFoundException() {
    VisitUpdateRequestDto updateReq = new VisitUpdateRequestDto();
    given(visitRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception =
        assertThrows(VisitNotFoundException.class, () -> visitService.updateVisit(1, updateReq));
    assertEquals("Visit with id=1 not found", exception.getMessage());
    verifyNoMoreInteractions(visitRepository);
  }

  @Test
  void updateVisit_UpdateStatus_Returns_UpdatedVisit() {
    VisitUpdateRequestDto updateReq = new VisitUpdateRequestDto(VisitStatus.CANCELLED, null);
    given(visitRepository.findById(1)).willReturn(Optional.of(visit));
    Visit updatedVisit =
        new Visit(
            1,
            LocalDateTime.of(2021, 7, 21, 15, 20),
            Duration.ofMinutes(15),
            VisitStatus.CANCELLED,
            BigDecimal.valueOf(50),
            animal,
            client,
            vet,
            office,
            "SomeDescriptiom");
    assertThat(visitService.updateVisit(1, updateReq)).isEqualTo(updatedVisit);
    verify(visitRepository, Mockito.times(1)).save(updatedVisit);
  }

  @Test
  void updateVisit_UpdateDescription_Returns_UpdatedVisit() {
    VisitUpdateRequestDto updateReq = new VisitUpdateRequestDto(null, "SomeNewDescription");
    given(visitRepository.findById(1)).willReturn(Optional.of(visit));
    Visit updatedVisit =
        new Visit(
            1,
            LocalDateTime.of(2021, 7, 21, 15, 20),
            Duration.ofMinutes(15),
            VisitStatus.PLANNED,
            BigDecimal.valueOf(50),
            animal,
            client,
            vet,
            office,
            "SomeNewDescription");
    assertThat(visitService.updateVisit(1, updateReq)).isEqualTo(updatedVisit);
    verify(visitRepository, Mockito.times(1)).save(updatedVisit);
  }

  @Test
  void updateVisit_UpdateDescriptionAndStatus_Returns_UpdatedVisit() {
    VisitUpdateRequestDto updateReq =
        new VisitUpdateRequestDto(VisitStatus.NOT_APPEARED, "SomeMoreNewDescription");
    given(visitRepository.findById(1)).willReturn(Optional.of(visit));
    Visit updatedVisit =
        new Visit(
            1,
            LocalDateTime.of(2021, 7, 21, 15, 20),
            Duration.ofMinutes(15),
            VisitStatus.NOT_APPEARED,
            BigDecimal.valueOf(50),
            animal,
            client,
            vet,
            office,
            "SomeMoreNewDescription");
    assertThat(visitService.updateVisit(1, updateReq)).isEqualTo(updatedVisit);
    verify(visitRepository, Mockito.times(1)).save(updatedVisit);
  }

  @Test
  void updateVisit_UpdateOtherField_Returns_NotUpdatedVisit() {
    VisitUpdateRequestDto updateReq = new VisitUpdateRequestDto(null, null);
    given(visitRepository.findById(1)).willReturn(Optional.of(visit));
    assertThat(visitService.updateVisit(1, updateReq)).isEqualTo(visit);
    verify(visitRepository, Mockito.times(1)).save(visit);
    //clock = n
  }

  @Test
  void findVisits_Returns_VisitLists() {}

  @Test
  void findVisits_WithPreferredVet_Returns_VisitLists() {}

  @Test
  void findVisits_InPast_Returns_EmptyList() {}

  @Test
  void findVisits_BeginInPast_Returns_VisitsList() {}

  @Test
  void findVisits_NoVetAvailable_Returns_EmptyList() {}

  @Test
  void findVisits_NoOfficeAvailable_Returns_EmptyList() {}

  @Test
  void findVisits_NoDatesAvailable_Returns_EmptyList() {}
}
