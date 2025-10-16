package com.example.clientapi.service.mapper;

import com.example.clientapi.dto.ContractDTO;
import com.example.clientapi.dto.ContractResponseDTO;
import com.example.clientapi.model.ClientEntity;
import com.example.clientapi.model.ContractEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-17T00:02:24+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ContractMapperImpl implements ContractMapper {

    @Override
    public ContractEntity ContractDtoToEntity(ContractDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ContractEntity contractEntity = new ContractEntity();

        contractEntity.setStartDate( dto.getStartDate() );
        contractEntity.setEndDate( dto.getEndDate() );
        contractEntity.setCostAmount( dto.getCostAmount() );

        return contractEntity;
    }

    @Override
    public ContractResponseDTO contractEntityToResponseDto(ContractEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ContractResponseDTO contractResponseDTO = new ContractResponseDTO();

        contractResponseDTO.setClientId( entityClientEntityId( entity ) );
        contractResponseDTO.setId( entity.getId() );
        contractResponseDTO.setStartDate( entity.getStartDate() );
        contractResponseDTO.setEndDate( entity.getEndDate() );
        contractResponseDTO.setCostAmount( entity.getCostAmount() );

        return contractResponseDTO;
    }

    private Long entityClientEntityId(ContractEntity contractEntity) {
        if ( contractEntity == null ) {
            return null;
        }
        ClientEntity clientEntity = contractEntity.getClientEntity();
        if ( clientEntity == null ) {
            return null;
        }
        Long id = clientEntity.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
