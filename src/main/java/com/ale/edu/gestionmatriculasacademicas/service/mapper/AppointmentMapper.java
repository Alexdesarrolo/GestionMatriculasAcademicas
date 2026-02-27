package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot;
import com.ale.edu.gestionmatriculasacademicas.domain.Student;
import com.ale.edu.gestionmatriculasacademicas.domain.Subject;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointment} and its DTO {@link AppointmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    @Mapping(target = "availableSlot", source = "availableSlot", qualifiedByName = "availableSlotId")
    @Mapping(target = "desiredSubjects", source = "desiredSubjects", qualifiedByName = "subjectNameSet")
    AppointmentDTO toDto(Appointment s);

    @Mapping(target = "removeDesiredSubjects", ignore = true)
    Appointment toEntity(AppointmentDTO appointmentDTO);

    @Named("studentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentDTO toDtoStudentId(Student student);

    @Named("availableSlotId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AvailableSlotDTO toDtoAvailableSlotId(AvailableSlot availableSlot);

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
