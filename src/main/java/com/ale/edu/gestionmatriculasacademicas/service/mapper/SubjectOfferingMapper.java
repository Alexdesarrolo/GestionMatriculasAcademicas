package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectOfferingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubjectOffering} and its DTO {@link SubjectOfferingDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubjectMapper.class, ProfessorMapper.class })
public interface SubjectOfferingMapper extends EntityMapper<SubjectOfferingDTO, SubjectOffering> {
    @Mapping(target = "enrolledCount", ignore = true)
    SubjectOfferingDTO toDto(SubjectOffering s);
}
