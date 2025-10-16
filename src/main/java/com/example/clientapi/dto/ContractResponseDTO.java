package com.example.clientapi.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ContractResponseDTO {
	private Long id;
	private Long clientId;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long costAmount;
}
