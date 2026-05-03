package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Professor;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProfessorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professor} and its DTO {@link ProfessorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessorMapper extends EntityMapper<ProfessorDTO, Professor> {}
