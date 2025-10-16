package com.example.clientapi.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClientCreateRequestDTO {
	@NotBlank @Pattern(regexp = "^(Company|Person)$", message = "clientType must be either 'Company' or 'Person'")
	private String clientType;
	@NotBlank @Pattern(regexp="^\\+?[1-9]\\d{1,14}$", message="Phone must be E.164, e.g. +41655552671")
	private String phone;
	@NotBlank @Email
	private String email;
	@NotBlank
	private String name;
	private LocalDate birthDate;
	private String companyIdentifier;

}
