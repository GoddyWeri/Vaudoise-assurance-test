package com.example.clientapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequestDTO {
	private LocalDate startDate;
	private LocalDate endDate;
	@NotNull @Positive
	private Long costAmount;

}
