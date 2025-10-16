package com.example.clientapi.service;

import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;

public interface ClientService {

	ClientResponseDTO createClient(ClientCreateRequestDTO clientDTO);
	
	ClientResponseDTO readClientById(Long clientId);
	
	ClientResponseDTO updateClient(Long clientId, ClientUpdateRequestDTO clientDTO);
	
	ClientResponseDTO deleteClientById(Long clientId);


}
