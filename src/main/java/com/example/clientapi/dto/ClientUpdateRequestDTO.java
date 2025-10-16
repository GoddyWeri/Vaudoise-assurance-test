package com.example.clientapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClientUpdateRequestDTO {
	  @Pattern(regexp = "^(Company|Person)$", message = "clientType must be either 'Company' or 'Person'")
	  private String clientType;
	  @Pattern(regexp="^\\+?[1-9]\\d{1,14}$", message="Phone must be E.164") 
	  private String phone;
	  @Email 
	  private String email;
	  private String name;
}
