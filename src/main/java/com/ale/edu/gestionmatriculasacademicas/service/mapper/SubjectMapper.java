package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.domain.Subject;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {

    @Mapping(target = "programs", source = "programs", qualifiedByName = "programNameSet")
    SubjectDTO toDto(Subject s);

    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "removePrograms", ignore = true)
    Subject toEntity(SubjectDTO subjectDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "removePrograms", ignore = true)
    void partialUpdate(@MappingTarget Subject entity, SubjectDTO dto);

    @Named("programName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProgramDTO toDtoProgramName(Program program);

    @Named("programNameSet")
    default Set<ProgramDTO> toDtoProgramNameSet(Set<Program> program) {
        return program.stream().map(this::toDtoProgramName).collect(Collectors.toSet());
    }
}
