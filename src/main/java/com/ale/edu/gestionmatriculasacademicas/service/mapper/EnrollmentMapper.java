package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Enrollment;
import com.ale.edu.gestionmatriculasacademicas.service.dto.EnrollmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enrollment} and its DTO {@link EnrollmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { StudentMapper.class, SubjectOfferingMapper.class })
public interface EnrollmentMapper extends EntityMapper<EnrollmentDTO, Enrollment> {}
