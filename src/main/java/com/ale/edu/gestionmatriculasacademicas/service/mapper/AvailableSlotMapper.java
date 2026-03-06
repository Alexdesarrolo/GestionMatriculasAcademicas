package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot;
import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AvailableSlotMapper extends EntityMapper<AvailableSlotDTO, AvailableSlot> {

    @Mapping(target = "program", source = "program", qualifiedByName = "programId")
    AvailableSlotDTO toDto(AvailableSlot s);

    @Mapping(target = "program", ignore = true)
    AvailableSlot toEntity(AvailableSlotDTO availableSlotDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "program", ignore = true)
    void partialUpdate(@MappingTarget AvailableSlot entity, AvailableSlotDTO dto);

    @Named("programId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "codePrefix", source = "codePrefix")
    ProgramDTO toDtoProgramId(Program program);
}