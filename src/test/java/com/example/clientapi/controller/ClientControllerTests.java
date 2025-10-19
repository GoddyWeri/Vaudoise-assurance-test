package com.example.clientapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;
import com.example.clientapi.exception.ClientNotFoundException;
import com.example.clientapi.service.ClientService;
import com.example.clientapi.service.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean 
    private ClientService clientService;

    @MockitoBean
    private ContractService contractService;
    
    
    @Test
    public void ClientController_CreateClient_ReturnCreated() throws Exception {
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
        
        given(clientService.createClient(any(ClientCreateRequestDTO.class)))
        .willReturn(clientResponseDTOMock);
        
        ResultActions response = mockMvc.perform(post("/clients/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientCreateRequestDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test 
    public void ClientController_ReadClientById_ReturnBadRequestErrorResponse() throws Exception {
        
        ClientUpdateRequestDTO clientUpdateRequestDTO = ClientUpdateRequestDTO.builder()
        		.clientType("Person")
                .name("john")
                .phone("yyy234354632")
			    .email("john@gmail.com")
                .build();
    	
        ClientResponseDTO clientResponseDTOMock = ClientResponseDTO.builder()
        		.clientType("Person")
                .name("john")
                .phone("234354632")
                .birthDate(LocalDate.parse("1945-02-23"))
			    .email("john@gmail.com")
                .build();
        
        given(clientService.updateClient(Mockito.anyLong(), any(ClientUpdateRequestDTO.class)))
        .willReturn(clientResponseDTOMock);
        
        ResultActions response = mockMvc.perform(patch("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientUpdateRequestDTO)));        

        response.andExpect(status().isBadRequest());
    }
    
    @Test
    public void ClientController_ReadClientById_ReturnNotFoundErrorResponse() throws Exception {
        Long clientId = 999L;
        String errorMessage = "Client with provided id does not exist in database.";

        given(clientService.readClientById(clientId))
            .willThrow(new ClientNotFoundException(errorMessage));

        ResultActions response = mockMvc.perform(get("/clients/{id}", clientId)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());
    }
    
    @Test
    public void ClientController_ReadClientById_ReturnAllClientFields() throws Exception {
        Long clientId = 1L;
        LocalDate today = LocalDate.now();
        
        ClientResponseDTO clientResponseDTOMock = ClientResponseDTO.builder()
        		.id(clientId)
        		.clientType("Person")
                .name("john")
                .phone("234354632")
                .birthDate(today.minusDays(33))
			    .email("john@gmail.com")
                .build();
        
        when(clientService.readClientById(clientId))
            .thenReturn(clientResponseDTOMock);

        ResultActions response = mockMvc.perform(get("/clients/{id}", clientId)
            .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(clientId))
        .andExpect(jsonPath("$.clientType").value("Person"))
        .andExpect(jsonPath("$.name").value("john"))
        .andExpect(jsonPath("$.phone").value("234354632"))
        .andExpect(jsonPath("$.birthDate").value(today.minusDays(33).toString()))
        .andExpect(jsonPath("$.email").value("john@gmail.com"))
        .andExpect(jsonPath("$.companyIdentifier").doesNotExist());    
        
    }

}
