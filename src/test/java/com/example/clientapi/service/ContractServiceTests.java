package com.example.clientapi.service;

import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.clientapi.dto.ContractRequestDTO;
import com.example.clientapi.dto.ContractResponseDTO;
import com.example.clientapi.exception.ContractDataInValidException;
import com.example.clientapi.model.ClientEntity;
import com.example.clientapi.model.ContractEntity;
import com.example.clientapi.repository.ClientRepository;
import com.example.clientapi.repository.ContractRepository;
import com.example.clientapi.service.impl.ContractServiceImpl;
import com.example.clientapi.service.mapper.ContractMapper;
import com.example.clientapi.utils.CustomUtils;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTests {
	@Mock
	private ContractRepository contractRepository;
	
	@Mock
	private ClientRepository clientRepository;
	
    @InjectMocks
    private ContractServiceImpl contractServiceImpl;
	
	@Mock
	private ContractMapper contractMapper;
	
	
	@Test
	public void ContractService_ContractRequestInfoChecks_ReturnsContractEntityForStorageWithStartDateSetToToday() throws Exception{	
        ClientEntity clientEntity = ClientEntity.builder()
        		.id(1L)
        		.clientType("Person")
                .name("Test Client")
                .phone("123456789")
                .email("test@example.com")
                .build();
		
		ContractRequestDTO contractRequestDTO = ContractRequestDTO.builder()
			    .startDate(null)
			    .endDate(null)
			    .costAmount(20L) 
			    .build();			

		when(clientRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(clientEntity) );
	    when(contractMapper.ContractDtoToEntity(contractRequestDTO)).thenReturn(new ContractEntity());
	    
		Class<?>[] argClasses = new Class[] { Long.class, ContractRequestDTO.class };
		Object[] argObjects = new Object[] { 1L, contractRequestDTO };
		
	    Method method = contractServiceImpl.getClass().getDeclaredMethod("contractRequestInfoChecks", argClasses);
	    method.setAccessible(true);
	    ContractEntity result = (ContractEntity) method.invoke(contractServiceImpl, argObjects);
	    
	    Assertions.assertThat(result).isNotNull();
	    Assertions.assertThat(result.getStartDate()).isEqualTo(LocalDate.now()); 
	    Assertions.assertThat(result.getUpdateDate()).isEqualTo(LocalDate.now());
	    Assertions.assertThat(result.getEndDate()).isNull(); 
	}
	
	
	@Test
	public void ContractService_FindAllClientContracts_ReturnsContractEntityPageForUpdateDateBetweenTwoDatesProvided() {
		
        ClientEntity clientEntity = ClientEntity.builder()
        		.id(1L)
        		.clientType("Person")
                .name("Test Client")
                .phone("123456789")
                .email("test@example.com")
                .build();
        
        
		ContractEntity contractEntity = ContractEntity.builder()
				.clientEntity(clientEntity)
			    .startDate(LocalDate.parse("2010-02-23"))
			    .endDate(LocalDate.parse("2022-02-23"))
			    .costAmount(20L) 
			    .updateDate(LocalDate.parse("2009-02-23"))
			    .build();
		
		ContractResponseDTO contractResponseDTO = ContractResponseDTO.builder()
				.clientId(1L)
				.id(20L)
			    .startDate(LocalDate.parse("2010-02-23"))
			    .endDate(LocalDate.parse("2022-02-23"))
			    .costAmount(20L) 
			    .build();
		
		//Making sure that any time repository function findByClientEntityIdAndUpdateDateBetween is called, we will always return a Page with element clientEntity. 
		//So we do not interact with DB.
		when(contractRepository.findByClientEntityIdAndUpdateDateBetweenAndEndDateAfter(Mockito.anyLong(), Mockito.any(LocalDate.class),  Mockito.any(LocalDate.class), Mockito.any(Pageable.class), Mockito.any(LocalDate.class)))
	    .thenReturn(new PageImpl<>(List.of(contractEntity), PageRequest.of(0, 10), 1));				

		//Handling the pre-client existence check
		when(clientRepository.existsById(Mockito.anyLong())).thenReturn(true);
		
        when(contractMapper.contractEntityToResponseDto(contractEntity)).thenReturn(contractResponseDTO);
		
		Page<ContractResponseDTO> foundContractDTOPage = contractServiceImpl.findAllClientContracts(1L, LocalDate.parse("2005-02-23"), LocalDate.parse("2012-02-23"), PageRequest.of(0, 10));
		
		Assertions.assertThat(foundContractDTOPage).isNotNull();
		Assertions.assertThat(foundContractDTOPage.getContent().get(0).getClientId()).isEqualTo(1L);
		
	}
	
	@Test
	public void ContractService_FindAllClientContracts_ReturnsContractEntityPageForNoDateRangeFilterProvided() {
		
        ClientEntity clientEntity = ClientEntity.builder()
        		.id(1L)
        		.clientType("Person")
                .name("Test Client")
                .phone("123456789")
                .email("test@example.com")
                .build();
        
        
		ContractEntity contractEntity = ContractEntity.builder()
				.clientEntity(clientEntity)
			    .startDate(LocalDate.parse("2010-02-23"))
			    .endDate(LocalDate.parse("2022-02-23"))
			    .costAmount(20L) 
			    .updateDate(LocalDate.parse("2009-02-23"))
			    .build();
		
		ContractResponseDTO contractResponseDTO = ContractResponseDTO.builder()
				.clientId(1L)
				.id(20L)
			    .startDate(LocalDate.parse("2010-02-23"))
			    .endDate(LocalDate.parse("2022-02-23"))
			    .costAmount(20L) 
			    .build();
		
		//Making sure that any time repository function findByClientEntityIdAndUpdateDateBetween is called, we will always return a Page with element clientEntity. 
		//So we do not interact with DB.
		when(contractRepository.findByClientEntityIdAndEndDateAfter(Mockito.anyLong(), Mockito.any(Pageable.class), Mockito.any(LocalDate.class)))
	    .thenReturn(new PageImpl<>(List.of(contractEntity), PageRequest.of(0, 10), 1));				

		//Handling the pre-client existence check
		when(clientRepository.existsById(Mockito.anyLong())).thenReturn(true);
		
        when(contractMapper.contractEntityToResponseDto(contractEntity)).thenReturn(contractResponseDTO);
		
		Page<ContractResponseDTO> foundContractDTOPage = contractServiceImpl.findAllClientContracts(1L, null, null, PageRequest.of(0, 10));
		
		Assertions.assertThat(foundContractDTOPage).isNotNull();
		
	}
	
	@Test
	public void ContractService_FindAllClientContracts_ThrowsExceptionForUpdatedAfterGreaterThanCurrentDate() {
		
		when(clientRepository.existsById(Mockito.anyLong())).thenReturn(true);
				
		      Assertions.assertThatThrownBy(() -> contractServiceImpl.findAllClientContracts(
		      1L, 
		      LocalDate.parse("2026-10-23"), 
		      LocalDate.parse("2024-03-01"), 
		      null
		))
		.isInstanceOf(ContractDataInValidException.class)
		.hasMessageContaining(CustomUtils.UPDATEAFTER_IN_FUTURE_ERROR_TEXT);
		
	}
	
	@Test
	public void ContractService_FindAllClientContracts_ThrowsExceptionForUpdatedAfterSetAfterUpdatedBeforeDate() {
		
		when(clientRepository.existsById(Mockito.anyLong())).thenReturn(true);
				
		      Assertions.assertThatThrownBy(() -> contractServiceImpl.findAllClientContracts(
		      1L, 
		      LocalDate.parse("2012-10-23"), 
		      LocalDate.parse("2010-03-01"), 
		      null
		))
		.isInstanceOf(ContractDataInValidException.class)
		.hasMessageContaining(CustomUtils.UPDATEBEFORE_AFTER_UPDATEAFTER_ERROR_TEXT);
		
	}
	
}
