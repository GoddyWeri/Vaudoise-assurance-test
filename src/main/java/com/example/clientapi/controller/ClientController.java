package com.example.clientapi.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;
import com.example.clientapi.dto.ContractDTO;
import com.example.clientapi.dto.ContractResponseDTO;
import com.example.clientapi.service.ClientService;
import com.example.clientapi.service.ContractService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientController {
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private ContractService contractService;
	
	@PostMapping("/")
	public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientCreateRequestDTO clientDTO) {
	    return ResponseEntity.ok(clientService.createClient(clientDTO));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClientResponseDTO> readClientById(@PathVariable Long id) {
	    return ResponseEntity.ok(clientService.readClientById(id));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientUpdateRequestDTO clientDTO) {
	    return ResponseEntity.ok(clientService.updateClient(id, clientDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ClientResponseDTO> deleteClientById(@PathVariable Long id) {
	    return ResponseEntity.ok(clientService.deleteClientById(id));
	}
	
	@PostMapping("/{clientId}/contracts")
	public ResponseEntity<ContractResponseDTO> createClientContract(@PathVariable Long clientId, @Valid @RequestBody ContractDTO contractDTO) {
	    return ResponseEntity.ok(contractService.createClientContract(clientId, contractDTO));
	}
	
	@PatchMapping("/contracts/{id}")
	public ResponseEntity<ContractResponseDTO> updateClientContract(@PathVariable Long id, @Valid @RequestBody ContractDTO contractDTO) {
	    return ResponseEntity.ok(contractService.updateClientContract(id, contractDTO));
	}
	
	@GetMapping("/{clientId}/contracts")
	public ResponseEntity<Page<ContractResponseDTO>> findAllClientContracts(@PathVariable Long clientId, @RequestParam(required = false) LocalDate updatedAfter, @RequestParam(required = false) LocalDate updatedBefore,
    @PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageableBody)
	{	
	    return ResponseEntity.ok(contractService.findAllClientContracts(clientId, updatedAfter, updatedBefore, pageableBody));
	}
	
	@GetMapping("/{clientId}/contracts/sumCosts")
	public ResponseEntity<Long> getTotalClientContractCosts(@PathVariable Long clientId) {
	    return ResponseEntity.ok(contractService.getTotalClientContractCosts(clientId));
	}
}
