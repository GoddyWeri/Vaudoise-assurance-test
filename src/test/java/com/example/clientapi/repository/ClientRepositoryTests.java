package com.example.clientapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.clientapi.model.ClientEntity;

@DataJpaTest
public class ClientRepositoryTests {
	@Autowired
	private ClientRepository clientRepository;
	
	@Test
	public void ClientRepository_CreateClient_ReturnSavedClient() {
		LocalDate today = LocalDate.now();
		ClientEntity clientEntity = ClientEntity.builder()
			    .name("John")
			    .clientType("Person")
			    .birthDate(today.minusDays(29)) 
			    .email("john@gmail.com")
			    .build();
		
		ClientEntity savedClientEntity = clientRepository.save(clientEntity);
		
		Assertions.assertThat(savedClientEntity).isNotNull();
		Assertions.assertThat(savedClientEntity.getEmail()).isNotNull();
		Assertions.assertThat(savedClientEntity.getEmail()).isEqualTo("john@gmail.com");
	}
	
	
    @Test
    public void ClientRepository_FindByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier_ReturnsAClientWhenMatchExists() {
		LocalDate today = LocalDate.now();
        ClientEntity client = ClientEntity.builder()
                .clientType("Person")
                .phone("123456789")
                .email("test@gem.com")
                .name("John")
                .birthDate(today.minusDays(30))
                .companyIdentifier(null) 
                .build();

        clientRepository.save(client);

        Optional<ClientEntity> result = clientRepository
                .findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(
                        "Person",
                        "123456789",
                        "test@gem.com",
                        "John",
                        today.minusDays(30),
                        null 
                );

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@gem.com");
    }

    @Test
    public void ClientRepository_FindByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier_ReturnsAClientWhenNoMatchExists() {
		LocalDate today = LocalDate.now();
        Optional<ClientEntity> result = clientRepository
                .findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(
                        "NonExistent",
                        "000000000",
                        "none@example.com",
                        "Ghost",
                        today.minusDays(30),
                        "XYZ999"
                );

        assertThat(result).isNotPresent();
    }
}
