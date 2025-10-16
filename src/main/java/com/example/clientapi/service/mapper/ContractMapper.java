package com.example.clientapi.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.clientapi.dto.ContractDTO;
import com.example.clientapi.dto.ContractResponseDTO;
import com.example.clientapi.model.ContractEntity;

@Mapper(componentModel = "spring")
public interface ContractMapper {
	  ContractEntity ContractDtoToEntity(ContractDTO dto);
	  
	  @Mapping(target = "clientId", source = "clientEntity.id")
	  ContractResponseDTO contractEntityToResponseDto(ContractEntity entity);	
	  
}
