package com.example.clientapi.service.mapper;

import com.example.clientapi.dto.ClientCreateRequestDTO;
import com.example.clientapi.dto.ClientResponseDTO;
import com.example.clientapi.dto.ClientUpdateRequestDTO;
import com.example.clientapi.model.ClientEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-17T00:02:24+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientEntity ClientCreateDtoToEntity(ClientCreateRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setClientType( dto.getClientType() );
        clientEntity.setPhone( dto.getPhone() );
        clientEntity.setEmail( dto.getEmail() );
        clientEntity.setName( dto.getName() );
        clientEntity.setBirthDate( dto.getBirthDate() );
        clientEntity.setCompanyIdentifier( dto.getCompanyIdentifier() );

        return clientEntity;
    }

    @Override
    public ClientResponseDTO ClientEntitytoResponseDto(ClientEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();

        clientResponseDTO.setId( entity.getId() );
        clientResponseDTO.setClientType( entity.getClientType() );
        clientResponseDTO.setPhone( entity.getPhone() );
        clientResponseDTO.setEmail( entity.getEmail() );
        clientResponseDTO.setName( entity.getName() );
        clientResponseDTO.setBirthDate( entity.getBirthDate() );
        clientResponseDTO.setCompanyIdentifier( entity.getCompanyIdentifier() );

        return clientResponseDTO;
    }

    @Override
    public void updateFromDto(ClientUpdateRequestDTO updatedClientDTO, ClientEntity foundClient) {
        if ( updatedClientDTO == null ) {
            return;
        }

        foundClient.setClientType( updatedClientDTO.getClientType() );
        foundClient.setPhone( updatedClientDTO.getPhone() );
        foundClient.setEmail( updatedClientDTO.getEmail() );
        foundClient.setName( updatedClientDTO.getName() );
    }
}
