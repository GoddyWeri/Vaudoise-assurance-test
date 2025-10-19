package com.example.clientapi.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;
import com.example.clientapi.model.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientMapper {
	  ClientEntity clientCreateDtoToEntity(ClientCreateRequestDTO dto);
	  ClientResponseDTO clientEntitytoResponseDto(ClientEntity entity);	  
	  void updateFromDto(ClientUpdateRequestDTO updatedClientDTO, @MappingTarget ClientEntity foundClient);
}
