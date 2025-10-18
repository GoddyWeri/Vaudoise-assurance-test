package com.example.clientapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.clientapi.model.ClientEntity;
import com.example.clientapi.model.ContractEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
class ContractRepositoryTests {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void ContractRepository_ContractRepository_FindByClientEntityIdAndUpdateDateBetweenAndEndDateAfter_ShouldReturnOnlyActiveContracts() {
        Long clientId = 1L;
        LocalDate today = LocalDate.now();

        ContractEntity activeContract1 = ContractEntity.builder()
            .clientEntity(ClientEntity.builder().id(clientId).build())
            .endDate(today.plusDays(10))
            .updateDate(today.minusDays(10))
            .build();
        
        ContractEntity activeContract2 = ContractEntity.builder()
                .clientEntity(ClientEntity.builder().id(clientId).build())
                .endDate(today.plusDays(11))
                .updateDate(today.minusDays(11))
                .build();
        
        ContractEntity activeContract3 = ContractEntity.builder()
                .clientEntity(ClientEntity.builder().id(clientId).build())
                .endDate(today.plusDays(12))
                .updateDate(today.minusDays(12))
                .build();

        ContractEntity expiredContract = ContractEntity.builder()
            .clientEntity(ClientEntity.builder().id(clientId).build())
            .endDate(today.minusDays(5))
            .updateDate(today.minusDays(13))
            .build();

        entityManager.persist(activeContract1);
        entityManager.persist(activeContract2);
        entityManager.persist(activeContract3);
        entityManager.persist(expiredContract);
        
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 10);
        Page<ContractEntity> result = contractRepository.findByClientEntityIdAndUpdateDateBetweenAndEndDateAfter(clientId, today.minusDays(13), today.minusDays(11), pageable, LocalDate.now());

        Assertions.assertThat(result.getContent())
            .hasSize(2)
            .allSatisfy(contract -> 
                Assertions.assertThat(contract.getEndDate()).isAfter(today));
    }
    
    @Test
    public void ContractRepository_SumContractCostsByClientId_ReturnsCorrectSum() {
        ClientEntity client = ClientEntity.builder()
                .clientType("Person")
                .name("Test")
                .build();
        entityManager.persist(client);

        ContractEntity contract1 = ContractEntity.builder()
                .clientEntity(client)
                .costAmount(100L)
                .build();
        ContractEntity contract2 = ContractEntity.builder()
                .clientEntity(client)
                .costAmount(200L)
                .build();
        entityManager.persist(contract1);
        entityManager.persist(contract2);
        entityManager.flush();

        Long sum = contractRepository.sumContractCostsByClientId(client.getId());

        assertThat(sum).isEqualTo(300L);
    }

    @Test
    public void SumContractCostsByClientId_ReturnsZeroWhenNoContractsExist() {
        ClientEntity client = ClientEntity.builder()
                .clientType("Person")
                .name("test")
                .build();
        entityManager.persist(client);
        entityManager.flush();

        Long sum = contractRepository.sumContractCostsByClientId(client.getId());

        assertThat(sum).isEqualTo(0L);
    }
}

