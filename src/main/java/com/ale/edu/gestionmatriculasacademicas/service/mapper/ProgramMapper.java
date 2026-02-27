package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.domain.Subject;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Program} and its DTO {@link ProgramDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgramMapper extends EntityMapper<ProgramDTO, Program> {
    @Mapping(target = "subjects", source = "subjects", qualifiedByName = "subjectNameSet")
    ProgramDTO toDto(Program s);

    @Mapping(target = "removeSubject", ignore = true)
    Program toEntity(ProgramDTO programDTO);

    @Named("subjectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SubjectDTO toDtoSubjectName(Subject subject);

    @Named("subjectNameSet")
    default Set<SubjectDTO> toDtoSubjectNameSet(Set<Subject> subject) {
        return subject.stream().map(this::toDtoSubjectName).collect(Collectors.toSet());
    }
}
