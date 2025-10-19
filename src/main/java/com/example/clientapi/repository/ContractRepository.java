package com.example.clientapi.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.clientapi.model.ContractEntity;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long>{
	
	 Page<ContractEntity> findAllByClientEntity_Id(Long clientId, Pageable pageable);

	 Page<ContractEntity> findByClientEntityIdAndEndDateAfter(Long clientId, Pageable pageable, LocalDate today);

	 Page<ContractEntity> findByClientEntityIdAndUpdateDateAfterAndEndDateAfter(Long clientId, LocalDate updatedAfter, Pageable pageable,  LocalDate today);

	 Page<ContractEntity> findByClientEntityIdAndUpdateDateBeforeAndEndDateAfter(Long clientId, LocalDate updatedBefore, Pageable pageable,  LocalDate today);

	 Page<ContractEntity> findByClientEntityIdAndUpdateDateBetweenAndEndDateAfter(Long clientId, LocalDate updatedAfter, LocalDate updatedBefore, Pageable pageable,  LocalDate today);
	 
	 @Query("SELECT COALESCE(SUM(c.costAmount), 0) FROM ContractEntity c WHERE c.clientEntity.id = :clientId")
	Long sumContractCostsByClientId(@Param("clientId") Long clientId);
}
