package com.example.clientapi.service.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.clientapi.dto.ContractRequestDTO;
import com.example.clientapi.dto.ContractResponseDTO;
import com.example.clientapi.exception.ClientNotFoundException;
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
	public ContractResponseDTO createClientContract(Long clientId, ContractRequestDTO contractDTO) {        
        ContractEntity contractEntityToCreate = contractRequestInfoChecks(clientId, contractDTO);
        log.info(CustomUtils.CREATING_CONTRACT_TEXT, contractDTO);
		ContractEntity contractEntity = contractRepository.save(contractEntityToCreate);
        log.info(CustomUtils.SUCCESS_CREATED_CONTRACT_TEXT);
		return contractMapper.contractEntityToResponseDto(contractEntity);
	}

	@Override
	public ContractResponseDTO updateClientContract(Long contractId, ContractRequestDTO updatedContractDTO) {
		ContractEntity originalContractEntity = contractRepository.findById(contractId)
				.orElseThrow(() -> {
				    log.error(CustomUtils.CONTRACT_ABSENT_ERROR_LOG, contractId);
				    return new ClientNotFoundException(CustomUtils.CONTRACT_ABSENT_ERROR);
				});
				
			originalContractEntity.setCostAmount(updatedContractDTO.getCostAmount());
			originalContractEntity.setUpdateDate(LocalDate.now());			
			ContractEntity updatedContractEntity = contractRepository.save(originalContractEntity);				
	        log.info(CustomUtils.SUCCESS_UPDATED_CONTRACT_TEXT, updatedContractDTO);
		return contractMapper.contractEntityToResponseDto(updatedContractEntity);
	}
	
	
	@Override
	public Page<ContractResponseDTO> findAllClientContracts(Long clientId, LocalDate updatedAfter, LocalDate updatedBefore,
	        Pageable pageableBody) {
	    if (!clientRepository.existsById(clientId)) {
		    log.error(CustomUtils.CLIENT_ABSENT_ERROR_LOG, clientId);
	        throw new ContractDataInValidException(CustomUtils.CLIENT_ABSENT_ERROR);
	    }

		checkProvidedDatesValidity(updatedAfter, updatedBefore);	
		Page<ContractEntity> contractsPage = queryForContractPage(updatedAfter, updatedBefore, clientId, pageableBody);  
        log.info(CustomUtils.SUCCESS_FINDING_ALL_CONTRACTS_TEXT, clientId);
	    return contractsPage.map(contractMapper::contractEntityToResponseDto);
	}
	
	
	@Override
	public Long getTotalClientContractCosts(Long clientId) {
	    if (!clientRepository.existsById(clientId)) {
		    log.error(CustomUtils.CLIENT_ABSENT_ERROR_LOG, clientId);
	        throw new ContractDataInValidException(CustomUtils.CLIENT_ABSENT_ERROR);
	    }
        log.info(CustomUtils.SUM_SUCCESS_TEXT);
	    return contractRepository.sumContractCostsByClientId(clientId);
	}

	
	private ContractEntity contractRequestInfoChecks(Long clientId, ContractRequestDTO contractDTO) {
		ClientEntity clientEntity = clientRepository.findById(clientId)
		    .orElseThrow(() -> {
		        log.error(CustomUtils.CLIENT_ABSENT_ERROR_LOG, clientId);
		        return new ClientNotFoundException(CustomUtils.CLIENT_FOR_CONTRACTT_ABSENT_ERROR);
		    });
		
		//we set the entity first, as the dates will be set depending on date values sent or omitted - hence preparing for database
		ContractEntity contractEntity = contractMapper.ContractDtoToEntity(contractDTO);
		LocalDate today = LocalDate.now();

		//Checking dates
		if(contractDTO.getStartDate() == null) {
			contractEntity.setStartDate(today);
		}else if(!contractDTO.getStartDate().toString().trim().isEmpty() && contractDTO.getStartDate().isBefore(today)) {
	        log.error(CustomUtils.START_DATE_EARLIER_ERROR);
		    throw new ContractDataInValidException(CustomUtils.START_DATE_EARLIER_ERROR);
		}
		
		if(contractDTO.getEndDate() != null) {
			if(!contractDTO.getEndDate().toString().trim().isEmpty() && (contractDTO.getEndDate().isBefore(contractEntity.getStartDate()) || contractDTO.getEndDate().isEqual(contractEntity.getStartDate()))) {
		        log.error(CustomUtils.END_DATE_EARLIER_TODAY_ERROR);
		        throw new ContractDataInValidException(CustomUtils.END_DATE_EARLIER_TODAY_ERROR);
			}else if(contractDTO.getEndDate().toString().trim().isEmpty()){
				contractEntity.setEndDate(null);
			}
		}	
		
		contractEntity.setUpdateDate(today);
		contractEntity.setClientEntity(clientEntity);
		
        log.debug(CustomUtils.CONTRACT_REQUEST_SUCCESSFULLY_CHECKED, contractDTO);
		
		return contractEntity;

	}

	//Block created to execute the best appropriate repository function, depending on passed in date ranges
	private Page<ContractEntity> queryForContractPage(LocalDate updatedAfter, LocalDate updatedBefore, Long clientId, Pageable pageableBody) {
		Page<ContractEntity> contractsPage;
		LocalDate today = LocalDate.now();
		
		if (updatedAfter == null && updatedBefore == null) {
			    contractsPage = contractRepository.findByClientEntityIdAndEndDateAfter(clientId, pageableBody, today); 

			} else if (updatedAfter != null  && updatedBefore == null ) {
			    contractsPage = contractRepository.findByClientEntityIdAndUpdateDateAfterAndEndDateAfter(clientId, updatedAfter, pageableBody, today);

			} else if (updatedAfter == null  && updatedBefore != null && !updatedBefore.toString().trim().isEmpty()) {
			    contractsPage = contractRepository.findByClientEntityIdAndUpdateDateBeforeAndEndDateAfter(clientId, updatedBefore, pageableBody, today);

			} else {
			    contractsPage = contractRepository.findByClientEntityIdAndUpdateDateBetweenAndEndDateAfter(clientId, updatedAfter, updatedBefore, pageableBody, today);
			}		
		return contractsPage;
	}

	private void checkProvidedDatesValidity(LocalDate updatedAfter, LocalDate updatedBefore) {
		LocalDate today = LocalDate.now();
		//minimum threshold date filter should always be less than or equal to today's date
	    if (updatedAfter != null && updatedAfter.isAfter(today)) {
		    log.error(CustomUtils.UPDATEAFTER_IN_FUTURE_ERROR_TEXT);
	        throw new ContractDataInValidException(CustomUtils.UPDATEAFTER_IN_FUTURE_ERROR_TEXT); 
	    }
		//maximum threshold date filter should always be less than or equal to today's date, because a contract could not have had an update in the future.
	    if (updatedBefore != null && updatedBefore.isAfter(today)) {
		    log.error(CustomUtils.UPDATEAFTER_IN_FUTURE_ERROR_TEXT);
	        throw new ContractDataInValidException(CustomUtils.UPDATEBEFORE_IN_FUTURE_ERROR_TEXT);
	    }
		//minimum threshold date filter should be always less than maximum threshold date filter
	    if (updatedAfter != null && updatedBefore != null && updatedBefore.isBefore(updatedAfter)) {
		    log.error(CustomUtils.UPDATEBEFORE_AFTER_UPDATEAFTER_ERROR_TEXT);
	        throw new ContractDataInValidException(CustomUtils.UPDATEBEFORE_AFTER_UPDATEAFTER_ERROR_TEXT);
	    }
		
	}

}
