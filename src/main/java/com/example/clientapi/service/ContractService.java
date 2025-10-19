package com.example.clientapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.clientapi.dto.ContractRequestDTO;
import com.example.clientapi.dto.ContractResponseDTO;

import jakarta.validation.Valid;

public interface ContractService {
	
	ContractResponseDTO createClientContract(Long clientId, ContractRequestDTO contractDTO);
	
	ContractResponseDTO updateClientContract(Long id, @Valid ContractRequestDTO contractDTO);
			
	Page<ContractResponseDTO> findAllClientContracts(Long clientId, LocalDate updatedAfter, LocalDate updatedBefore, Pageable pageableBody);

	Long getTotalClientContractCosts(Long clientId);

}
