package com.example.clientapi.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClientResponseDTO { 
	private Long id;
	private String clientType;
	private String phone;
	private String email;
	private String name;
	private LocalDate birthDate;
	private String companyIdentifier;
}
