package uj.jwzp2021.gp.VetApp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp2021.gp.VetApp.exception.VeterinaryAppException;
import uj.jwzp2021.gp.VetApp.exception.animal.AnimalNotFoundException;
import uj.jwzp2021.gp.VetApp.exception.client.ClientNotFoundException;
import uj.jwzp2021.gp.VetApp.mapper.ClientMapper;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.AnimalRequestDto;
import uj.jwzp2021.gp.VetApp.model.dto.Requests.ClientRequestDto;
import uj.jwzp2021.gp.VetApp.model.entity.AnimalType;
import uj.jwzp2021.gp.VetApp.model.entity.Client;
import uj.jwzp2021.gp.VetApp.repository.ClientRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

  @InjectMocks
  private ClientService clientService;
  @Mock
  private ClientRepository clientRepository;
  @Mock
  private ClientMapper clientMapper;

  private static Client client;
  private static Client client2;

  @BeforeAll
  static void setUp(){
    client = new Client(1, "Jan", "Kowalski");
    client2 = new Client(2, "Anna", "Nowak");
  }


  @Test
  void getClientById_ValidId_Returns_Client() {
    given(clientRepository.findById(1)).willReturn(Optional.of(client));
    assertThat(clientService.getClientById(1)).isNotNull().isEqualTo(client);
  }

  @Test
  void getClientById_invalidId_Throws_ClientNotFoundException() {
    given(clientRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1));
    assertEquals("Client with id=1 not found", exception.getMessage());
  }

  @Test
  void getAll_Returns_ListOfClients() {
    given(clientRepository.findAll()).willReturn(List.of(client2, client));
    assertThat(clientService.getAll()).isNotNull().hasSize(2).containsExactlyInAnyOrderElementsOf(List.of(client, client2));
  }

  @Test
  void getAll_Returns_ListOfOneClient() {
    given(clientRepository.findAll()).willReturn(List.of(client));
    assertThat(clientService.getAll()).isNotNull().hasSize(1).containsExactlyInAnyOrderElementsOf(List.of(client));
  }

  @Test
  void getAll_Returns_EmptyList() {
    given(clientRepository.findAll()).willReturn(Collections.emptyList());
    assertThat(clientService.getAll()).isNotNull().isEmpty();
  }

  @Test
  void deleteClient_ClientExists_DeleteAndReturns_Client() {
    given(clientRepository.findById(1)).willReturn(Optional.of(client));
    assertThat(clientService.deleteClient(1)).isEqualTo(client);
    verify(clientRepository, Mockito.times(1)).delete(client);
    verify(clientRepository, Mockito.times(1)).findById(1);
  }

  @Test
  void deleteClient_ClientNotExists_Throws_ClientNotFoundException() {
    given(clientRepository.findById(1)).willReturn(Optional.empty());
    VeterinaryAppException exception = assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1));
    assertEquals("Client with id=1 not found", exception.getMessage());
    verifyNoMoreInteractions(clientRepository);
  }

  @Test
  void createClient_ReturnsAndSave_Animal() {
    ClientRequestDto clientReq = new ClientRequestDto("Jan", "Kowalski");
    given(clientMapper.toClient(clientReq)).willReturn(client);
    given(clientRepository.save(client)).willReturn(client);

    assertThat(clientService.createClient(clientReq)).isNotNull().isEqualTo(client);
    verify(clientRepository, Mockito.times(1)).save(client);
  }
}