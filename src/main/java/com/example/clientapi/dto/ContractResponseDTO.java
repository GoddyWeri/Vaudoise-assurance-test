package com.example.clientapi.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractResponseDTO {
	private Long id;
	private Long clientId;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long costAmount;
}
