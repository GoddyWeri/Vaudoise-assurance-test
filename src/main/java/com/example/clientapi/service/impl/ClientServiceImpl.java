package com.example.clientapi.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;
import com.example.clientapi.exception.ClientDataBaseInfoException;
import com.example.clientapi.exception.ClientInvalidDataException;
import com.example.clientapi.model.ClientEntity;
import com.example.clientapi.model.ContractEntity;
import com.example.clientapi.repository.ClientRepository;
import com.example.clientapi.repository.ContractRepository;
import com.example.clientapi.service.ClientService;
import com.example.clientapi.service.mapper.ClientMapper;
import com.example.clientapi.utils.CustomUtils;


@Service
public class ClientServiceImpl implements ClientService{
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private ClientMapper clientMapper;

	@Override
	public ClientResponseDTO createClient(ClientCreateRequestDTO clientDTO) {
		clientInfoChecks(clientDTO);
		ClientEntity clientEntity = clientRepository.save(clientMapper.ClientCreateDtoToEntity(clientDTO));
		return clientMapper.ClientEntitytoResponseDto(clientEntity);
	}

	@Override
	public ClientResponseDTO readClientById(Long clientId) {
		ClientEntity client = clientRepository.findById(clientId)
				.orElseThrow(() -> new ClientDataBaseInfoException(CustomUtils.CLIENT_ABSENT_ERROR));	
		return clientMapper.ClientEntitytoResponseDto(client);
	}

	@Override
	public ClientResponseDTO updateClient(Long clientId, ClientUpdateRequestDTO updatedClientDTO) {
		ClientEntity foundClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientDataBaseInfoException(CustomUtils.CLIENT_ABSENT_ERROR));

        clientMapper.updateFromDto(updatedClientDTO, foundClient);
        
        clientRepository.findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(foundClient.getClientType(), foundClient.getPhone(), foundClient.getEmail(), foundClient.getName(), foundClient.getBirthDate(), foundClient.getCompanyIdentifier())
        .ifPresent(existing -> {
            throw new ClientDataBaseInfoException(CustomUtils.CLIENT_DATA_CONFLICT_ERROR);
        });
        
        ClientEntity updatedEntity = clientRepository.save(foundClient);
        return clientMapper.ClientEntitytoResponseDto(updatedEntity);
	}

	@Override
	@Transactional
	public ClientResponseDTO deleteClientById(Long clientId) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientDataBaseInfoException(CustomUtils.CLIENT_ABSENT_ERROR));
        
        clientRepository.delete(client);
        
        return clientMapper.ClientEntitytoResponseDto(client);
	}
	
	private void clientInfoChecks(ClientCreateRequestDTO clientDTO) {
		if(clientDTO.getClientType().equals(CustomUtils.PERSON_TEXT) && (clientDTO.getCompanyIdentifier() != null && !clientDTO.getCompanyIdentifier().equals(CustomUtils.EMPTY_STRING))) {
			throw new ClientInvalidDataException(CustomUtils.PERSON_COMPANY_ID_ERROR);
		}
		if(clientDTO.getClientType().equals(CustomUtils.COMPANY_TEXT) && (clientDTO.getBirthDate() != null && !clientDTO.getBirthDate().toString().trim().isEmpty())) {
			throw new ClientInvalidDataException(CustomUtils.COMPANY_BDAY_ERROR);
		}
		
		clientRepository.findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(clientDTO.getClientType(), clientDTO.getPhone(), clientDTO.getEmail(), clientDTO.getName(), clientDTO.getBirthDate(), clientDTO.getCompanyIdentifier())
			    .ifPresent(existing -> {
			        throw new ClientDataBaseInfoException(CustomUtils.CLIENT_DATA_CONFLICT_ERROR);
			    });	
	}

}
