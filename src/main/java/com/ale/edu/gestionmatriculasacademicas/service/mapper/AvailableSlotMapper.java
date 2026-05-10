package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot;
import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AvailableSlotMapper extends EntityMapper<AvailableSlotDTO, AvailableSlot> {

    @Mapping(target = "program", source = "program", qualifiedByName = "programId")
    @Mapping(target = "advisors", source = "advisors", qualifiedByName = "fullSet")
    AvailableSlotDTO toDto(AvailableSlot s);

    @Mapping(target = "program", ignore = true)
    @Mapping(target = "advisors", ignore = true)
    AvailableSlot toEntity(AvailableSlotDTO availableSlotDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "advisors", ignore = true)
    void partialUpdate(@MappingTarget AvailableSlot entity, AvailableSlotDTO dto);

    @Named("programId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "codePrefix", source = "codePrefix")
    ProgramDTO toDtoProgramId(Program program);

}