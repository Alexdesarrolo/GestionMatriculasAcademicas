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

@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {

    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    @Mapping(target = "availableSlot", source = "availableSlot", qualifiedByName = "availableSlotId")
    @Mapping(target = "desiredSubjects", source = "desiredSubjects", qualifiedByName = "subjectNameSet")
    AppointmentDTO toDto(Appointment s);

    @Mapping(target = "removeDesiredSubjects", ignore = true)
    @Mapping(target = "desiredSubjects", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "availableSlot", ignore = true)
    Appointment toEntity(AppointmentDTO appointmentDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "availableSlot", ignore = true)
    @Mapping(target = "removeDesiredSubjects", ignore = true)
    @Mapping(target = "desiredSubjects", ignore = true)
    void partialUpdate(@MappingTarget Appointment entity, AppointmentDTO dto);

    @Named("studentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "studentCode", source = "studentCode")
    @Mapping(target = "nationalId", source = "nationalId")
    @Mapping(target = "active", source = "active")
    StudentDTO toDtoStudentId(Student student);

    @Named("availableSlotId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "availableSpots", source = "availableSpots")
    @Mapping(target = "bookedSpots", source = "bookedSpots")
    @Mapping(target = "active", source = "active")
    AvailableSlotDTO toDtoAvailableSlotId(AvailableSlot availableSlot);

    @Named("subjectName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "credits", source = "credits")
    SubjectDTO toDtoSubjectName(Subject subject);

    @Named("subjectNameSet")
    default Set<SubjectDTO> toDtoSubjectNameSet(Set<Subject> subject) {
        return subject.stream().map(this::toDtoSubjectName).collect(Collectors.toSet());
    }
}