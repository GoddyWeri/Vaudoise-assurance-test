package com.example.clientapi.service.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clientapi.dto.ContractDTO;
import com.example.clientapi.dto.ContractResponseDTO;
import com.example.clientapi.exception.ClientDataBaseInfoException;
import com.example.clientapi.exception.ContractDataInValidException;
import com.example.clientapi.model.ClientEntity;
import com.example.clientapi.model.ContractEntity;
import com.example.clientapi.repository.ClientRepository;
import com.example.clientapi.repository.ContractRepository;
import com.example.clientapi.service.ContractService;
import com.example.clientapi.service.mapper.ContractMapper;
import com.example.clientapi.utils.CustomUtils;

@Service
public class ContractServiceImpl implements ContractService{
	
    private static final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private ContractMapper contractMapper;
	
	@Override
	public ContractResponseDTO createClientContract(Long clientId, ContractDTO contractDTO) {
        log.info("Starting contract creation process...");
        
        ContractEntity contractEntityToCreate = contractInfoChecks(clientId, contractDTO);
		ContractEntity contractEntity = contractRepository.save(contractEntityToCreate);
		return contractMapper.contractEntityToResponseDto(contractEntity);
	}

	@Override
	public ContractResponseDTO updateClientContract(Long contractId, ContractDTO updatedContractDTO) {
		ContractEntity foundContractEntity = contractRepository.findById(contractId)
		        .orElseThrow(() -> new ClientDataBaseInfoException(CustomUtils.CONTRACT_ABSENT_ERROR));	
				
			foundContractEntity.setCostAmount(updatedContractDTO.getCostAmount());
			foundContractEntity.setUpdateDate(LocalDate.now());			
			ContractEntity contractEntity = contractRepository.save(foundContractEntity);		
		return contractMapper.contractEntityToResponseDto(contractEntity);
	}

	
	private ContractEntity contractInfoChecks(Long clientId, ContractDTO contractDTO) {
		ClientEntity clientEntity = clientRepository.findById(clientId)
        .orElseThrow(() -> new ClientDataBaseInfoException(CustomUtils.CLIENT_FOR_CONTRACTT_ABSENT_ERROR));	
		
		//we set the entity first, as the dates will be set depending on date values sent or omitted - hence preparing for database
		ContractEntity contractEntity = contractMapper.ContractDtoToEntity(contractDTO);
		LocalDate today = LocalDate.now();

		//Checking dates
		if (contractDTO.getStartDate() != null && !contractDTO.getStartDate().toString().trim().isEmpty()) {
			if (contractDTO.getStartDate().isBefore(today)) {
			    throw new ContractDataInValidException(CustomUtils.START_DATE_EARLIER_ERROR);
			}
		}else{
			contractEntity.setStartDate(today);
		}

		if (contractDTO.getEndDate() != null && !contractDTO.getEndDate().toString().trim().isEmpty()) {
		    if (contractDTO.getEndDate().isBefore(contractDTO.getStartDate()) || contractDTO.getEndDate().isEqual(contractDTO.getStartDate())) {
		        throw new ContractDataInValidException(CustomUtils.END_DATE_EARLIER_TODAY_ERROR);
		    }
		}else if(contractDTO.getEndDate().toString().trim().isEmpty()){
			contractEntity.setEndDate(null);
		}
		
		contractEntity.setUpdateDate(today);
		contractEntity.setClientEntity(clientEntity);
		
		return contractEntity;

	}

	@Override
	public Page<ContractResponseDTO> findAllClientContracts(Long clientId, LocalDate updatedAfter, LocalDate updatedBefore,
	        Pageable pageableBody) {
	    if (!clientRepository.existsById(clientId)) {
	        throw new RuntimeException("Client not found");
	    }
	    

		checkProvidedDatesValidity(updatedAfter, updatedBefore);

		
		Page<ContractEntity> contractsPage = queryForContractPage(updatedAfter, updatedBefore, clientId, pageableBody);

	    	    
	    return contractsPage.map(contractMapper::contractEntityToResponseDto);
	}

	private Page<ContractEntity> queryForContractPage(LocalDate updatedAfter, LocalDate updatedBefore, Long clientId, Pageable pageableBody) {
		Page<ContractEntity> contractsPage;
		
		if (updatedAfter == null && updatedBefore == null) {
			    contractsPage = contractRepository.findByClientEntityId(clientId, pageableBody);

			} else if (updatedAfter != null  && updatedBefore == null ) {
			    contractsPage = contractRepository.findByClientEntityIdAndUpdateDateAfter(clientId, updatedAfter, pageableBody);

			} else if (updatedAfter == null  && updatedBefore != null && !updatedBefore.toString().trim().isEmpty()) {
			    contractsPage = contractRepository.findByClientEntityIdAndUpdateDateBefore(clientId, updatedBefore, pageableBody);

			} else {
			    contractsPage = contractRepository.findByClientEntityIdAndUpdateDateBetween(clientId, updatedAfter, updatedBefore, pageableBody);
			}		
		return contractsPage;
	}

	private void checkProvidedDatesValidity(LocalDate updatedAfter, LocalDate updatedBefore) {
		LocalDate today = LocalDate.now();
	    if (updatedAfter != null && updatedAfter.isAfter(today)) {
	        throw new ContractDataInValidException("updatedAfter cannot be in the future");
	    }
	    if (updatedBefore != null && updatedBefore.isAfter(today)) {
	        throw new ContractDataInValidException("updatedBefore cannot be in the future");
	    }
	    if (updatedAfter != null && updatedBefore != null && updatedBefore.isBefore(updatedAfter)) {
	        throw new ContractDataInValidException("updatedBefore must be after or equal to updatedAfter");
	    }
		
	}

	@Override
	public Long getTotalClientContractCosts(Long clientId) {
		// TODO Auto-generated method stub
		return null;
	}

}
