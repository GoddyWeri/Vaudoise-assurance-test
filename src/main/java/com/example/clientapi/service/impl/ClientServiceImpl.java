package com.example.clientapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;
import com.example.clientapi.exception.ClientNotFoundException;
import com.example.clientapi.exception.ClientDataConflictException;
import com.example.clientapi.exception.ClientInvalidDataException;
import com.example.clientapi.model.ClientEntity;
import com.example.clientapi.repository.ClientRepository;
import com.example.clientapi.service.ClientService;
import com.example.clientapi.service.mapper.ClientMapper;
import com.example.clientapi.utils.CustomUtils;


@Service
public class ClientServiceImpl implements ClientService{
    private static final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ClientMapper clientMapper;

	@Override
	public ClientResponseDTO createClient(ClientCreateRequestDTO clientDTO) {
        log.info(CustomUtils.CREATING_CLIENT_TEXT, clientDTO);
		clientRequestDataValidations(clientDTO);
		ClientEntity clientEntity = clientRepository.save(clientMapper.clientCreateDtoToEntity(clientDTO));
        log.info(CustomUtils.SUCCESS_CREATED_CLIENT_TEXT);
		return clientMapper.clientEntitytoResponseDto(clientEntity);
	}

	@Override
	public ClientResponseDTO readClientById(Long clientId) {
		ClientEntity client = clientRepository.findById(clientId)
		    .orElseThrow(() -> {
		        log.error(CustomUtils.CLIENT_ABSENT_ERROR_LOG, clientId);
		        return new ClientNotFoundException(CustomUtils.CLIENT_ABSENT_ERROR);
		    });
		
        log.info(CustomUtils.SUCCESS_CLIENT_FOUND_LOG, clientId);
		return clientMapper.clientEntitytoResponseDto(client);
	}

	@Override
	public ClientResponseDTO updateClient(Long clientId, ClientUpdateRequestDTO updatedClientDTO) {
		ClientEntity foundClient = clientRepository.findById(clientId)
			    .orElseThrow(() -> {
			        log.error(CustomUtils.CLIENT_ABSENT_ERROR_LOG, clientId);
			        return new ClientNotFoundException(CustomUtils.CLIENT_ABSENT_ERROR);
			    });
		
		if(!foundClient.getClientType().equals(updatedClientDTO.getClientType())) {
	        log.error(CustomUtils.CLIENT_TYPE_CHANGE_ERROR);
            throw new ClientInvalidDataException(CustomUtils.CLIENT_TYPE_CHANGE_ERROR);
		}

        clientMapper.updateFromDto(updatedClientDTO, foundClient);
        
        //Re-check if not a similar client in database already
        clientRepository.findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(foundClient.getClientType(), foundClient.getPhone(), foundClient.getEmail(), foundClient.getName(), foundClient.getBirthDate(), foundClient.getCompanyIdentifier())
        .ifPresent(existing -> {
	        log.error(CustomUtils.CLIENT_DATA_CONFLICT_ERROR);
            throw new ClientDataConflictException(CustomUtils.CLIENT_DATA_CONFLICT_ERROR);
        });
                
        ClientEntity updatedEntity = clientRepository.save(foundClient);
        log.info(CustomUtils.UPDATING_CLIENT_SUCCESS_LOG, clientId, updatedClientDTO);

        return clientMapper.clientEntitytoResponseDto(updatedEntity);
	}

	@Override
	@Transactional
	public ClientResponseDTO deleteClientById(Long clientId) {
        log.info(CustomUtils.DELETING_CLIENT_LOG, clientId);
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(CustomUtils.CLIENT_ABSENT_ERROR));
        
        //the contracts associated to the client are updated in ClientEntity PreDelete - clientId dissociated and update date set to today(day of deletion)
        clientRepository.delete(client);
        log.info(CustomUtils.DELETE_CLIENT_SUCCESS_LOG, clientId);
        
        return clientMapper.clientEntitytoResponseDto(client); 
	}
	
	private void clientRequestDataValidations(ClientCreateRequestDTO clientDTO) {
		//Client of type person cannot have company identifier
		if(clientDTO.getClientType().equals(CustomUtils.PERSON_TEXT) && (clientDTO.getCompanyIdentifier() != null && !clientDTO.getCompanyIdentifier().equals(CustomUtils.EMPTY_STRING))) {
	        log.error(CustomUtils.PERSON_COMPANY_ID_ERROR);
			throw new ClientInvalidDataException(CustomUtils.PERSON_COMPANY_ID_ERROR);
		}
		//Client of type person must be created with birth date
		else if(clientDTO.getClientType().equals(CustomUtils.PERSON_TEXT) && (clientDTO.getBirthDate() == null || clientDTO.getBirthDate().toString().equals(CustomUtils.EMPTY_STRING))) {
	        log.error(CustomUtils.BIRTH_DAY_REQUIRED_ERROR_LOG);
			throw new ClientInvalidDataException(CustomUtils.BIRTH_DAY_REQUIRED_ERROR);
		}
		//Client of type Company cannot have birth date
		if(clientDTO.getClientType().equals(CustomUtils.COMPANY_TEXT) && (clientDTO.getBirthDate() != null && !clientDTO.getBirthDate().toString().trim().isEmpty())) {
	        log.error(CustomUtils.COMPANY_BDAY_ERROR);
			throw new ClientInvalidDataException(CustomUtils.COMPANY_BDAY_ERROR);
		}
		//Client of type Company cannot must be provided with a company identifier for creation
		else if(clientDTO.getClientType().equals(CustomUtils.COMPANY_TEXT) && (clientDTO.getCompanyIdentifier() == null || clientDTO.getCompanyIdentifier().toString().equals(CustomUtils.EMPTY_STRING))) {
	        log.error(CustomUtils.COMPANY_IDENTIFIER_REQUIRED_ERROR_LOG);
			throw new ClientInvalidDataException(CustomUtils.COMPANY_ID_REQUIRED_ERROR);
		}
		
		clientRepository.findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(clientDTO.getClientType(), clientDTO.getPhone(), clientDTO.getEmail(), clientDTO.getName(), clientDTO.getBirthDate(), clientDTO.getCompanyIdentifier())
			    .ifPresent(existing -> {
			        log.error(CustomUtils.CLIENT_DATA_CONFLICT_ERROR_LOG, clientDTO);
			        throw new ClientDataConflictException(CustomUtils.CLIENT_DATA_CONFLICT_ERROR);
			    });	
		
		if(clientDTO.getClientType().equals(CustomUtils.PERSON_TEXT)) {
			clientDTO.setCompanyIdentifier(null);
		}
	}

}
