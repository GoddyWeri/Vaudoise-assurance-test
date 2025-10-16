package com.example.clientapi.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clientapi.model.ContractEntity;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long>{
	
	 Page<ContractEntity> findAllByClientEntity_Id(Long clientId, Pageable pageable);

	 Page<ContractEntity> findByClientEntityId(Long clientId, Pageable pageable);

	 Page<ContractEntity> findByClientEntityIdAndUpdateDateAfter(Long clientId, LocalDate updatedAfter, Pageable pageable);

	 Page<ContractEntity> findByClientEntityIdAndUpdateDateBefore(Long clientId, LocalDate updatedBefore, Pageable pageable);

	 Page<ContractEntity> findByClientEntityIdAndUpdateDateBetween(Long clientId, LocalDate updatedAfter, LocalDate updatedBefore, Pageable pageable);
}
