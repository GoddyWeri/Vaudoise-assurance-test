package com.example.clientapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ContractDTO {
	private LocalDate startDate;
	private LocalDate endDate;
	@NotNull @Positive
	private Long costAmount;

}
