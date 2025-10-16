package com.example.clientapi.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clientapi.model.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long>{
	Optional<ClientEntity> findByClientTypeAndPhoneAndEmailAndNameAndBirthDateAndCompanyIdentifier(
	        String clientType,
	        String phone,
	        String email,
	        String name,
	        LocalDate birthDate,
	        String companyIdentifier
	    );
}
