package com.example.clientapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;
import com.example.clientapi.exception.ClientDataConflictException;
import com.example.clientapi.exception.ClientInvalidDataException;
import com.example.clientapi.model.ClientEntity;
import com.example.clientapi.model.ContractEntity;
import com.example.clientapi.repository.ClientRepository;
import com.example.clientapi.service.impl.ClientServiceImpl;
import com.example.clientapi.service.mapper.ClientMapper;
import com.example.clientapi.utils.CustomUtils;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;
    
    @Mock
    private ClientMapper clientMapper; 


    @Test
    public void ClientService_CreatClient_ReturnsClientDto() {
        ClientEntity clientEntity = ClientEntity.builder()
        		.clientType("Person")
                .name("john")
                .phone("234354632")
                .birthDate(LocalDate.parse("1945-02-23"))
			    .email("john@gmail.com")
                .build();
        
        ClientCreateRequestDTO clientCreateRequestDTO = ClientCreateRequestDTO.builder()
        		.clientType("Person")
                .name("john")
                .phone("234354632")
                .birthDate(LocalDate.parse("1945-02-23"))
			    .email("john@gmail.com")
                .build();
        
        ClientResponseDTO clientResponseDTOMock = ClientResponseDTO.builder()
        		.clientType("Person")
                .name("john")
                .phone("234354632")
                .birthDate(LocalDate.parse("1945-02-23"))
			    .email("john@gmail.com")
                .build();

        when(clientMapper.clientCreateDtoToEntity(clientCreateRequestDTO)).thenReturn(clientEntity);
        when(clientRepository.save(Mockito.any(ClientEntity.class))).thenReturn(clientEntity);
        when(clientMapper.clientEntitytoResponseDto(clientEntity)).thenReturn(clientResponseDTOMock);
        
        ClientResponseDTO clientResponseDTO = clientServiceImpl.createClient(clientCreateRequestDTO);

        Assertions.assertThat(clientResponseDTO).isNotNull();
    }
    
    @Test
    public void ClientService_CreateClient_ThrowsExceptionWhenCompanyIdProvidedForPerson() {
        ClientCreateRequestDTO invalidDto = ClientCreateRequestDTO.builder()
            .name("john")
            .clientType("Person")
            .companyIdentifier("idTest12")
            .birthDate(LocalDate.parse("1945-02-23"))
            .build();

        Assertions.assertThatThrownBy(() -> clientServiceImpl.createClient(invalidDto))
            .isInstanceOf(ClientInvalidDataException.class)
            .hasMessageContaining(CustomUtils.PERSON_COMPANY_ID_ERROR);
    }
    
    @Test 
    public void ClientService_CreateClient_ThrowsExceptionWhenBirthDayProvidedForCompany() {
        ClientCreateRequestDTO invalidDto = ClientCreateRequestDTO.builder()
            .name("textile")
            .clientType("Company")
            .companyIdentifier("idTest12")
            .birthDate(LocalDate.parse("1945-02-23"))
            .build();

        Assertions.assertThatThrownBy(() -> clientServiceImpl.createClient(invalidDto))
            .isInstanceOf(ClientInvalidDataException.class)
            .hasMessageContaining(CustomUtils.COMPANY_BDAY_ERROR);
    }
    
    @Test
    public void ClientService_UpdateClient_ThrowsExceptionWhenClientWithSameDataExistsInDB() {
    	Long clientIdSent = 1L;
        ClientUpdateRequestDTO updatedDto = ClientUpdateRequestDTO.builder()
            .name("Elvis")
            .clientType("Person")
            .phone("234354632")
		    .email("textile@gmail.com")
            .build();
        
        ClientEntity foundClientToBeUpdated = ClientEntity.builder()
        		.clientType("Person")
                .name("john")
                .phone("234354632")
                .birthDate(LocalDate.parse("1945-02-23"))
			    .email("john@gmail.com")
                .build();
        
        ClientEntity clientEntityFoundWithSameParamsInDb = ClientEntity.builder()
        		.clientType("Person")
                .name("Elvis")
                .phone("234354632")
			    .email("textile@gmail.com")
                .build();
        
        when(clientRepository.findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(
        		foundClientToBeUpdated.getClientType(),
                foundClientToBeUpdated.getPhone(),
                foundClientToBeUpdated.getEmail(),
                foundClientToBeUpdated.getName(),
                foundClientToBeUpdated.getBirthDate(),
                foundClientToBeUpdated.getCompanyIdentifier()
        )).thenReturn(Optional.of(clientEntityFoundWithSameParamsInDb));
        
        when(clientRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(foundClientToBeUpdated));  
        doNothing().when(clientMapper).updateFromDto(updatedDto, foundClientToBeUpdated);
        
        Assertions.assertThatThrownBy(() -> clientServiceImpl.updateClient(clientIdSent, updatedDto))
            .isInstanceOf(ClientDataConflictException.class)
            .hasMessageContaining(CustomUtils.CLIENT_DATA_CONFLICT_ERROR);
    }

    @Test
    public void ClientService_UpdateClient_ThrowsExceptionWhenTryingToUpdateClientType() {
    	Long clientIdSent = 1L;
        ClientUpdateRequestDTO updatedDto = ClientUpdateRequestDTO.builder()
            .name("Elvis")
            .clientType("Person")
            .phone("234354632")
		    .email("textile@gmail.com")
            .build();
        
        ClientEntity foundClientToBeUpdated = ClientEntity.builder()
        		.clientType("Company")
                .name("companyName")
                .phone("234354632")
                .birthDate(LocalDate.parse("1945-02-23"))
			    .email("john@gmail.com")
                .build();

        
        when(clientRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(foundClientToBeUpdated));  
        
        Assertions.assertThatThrownBy(() -> clientServiceImpl.updateClient(clientIdSent, updatedDto))
            .isInstanceOf(ClientInvalidDataException.class)
            .hasMessageContaining(CustomUtils.CLIENT_TYPE_CHANGE_ERROR);
    }
   

}
