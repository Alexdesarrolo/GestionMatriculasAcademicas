package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot;
import com.ale.edu.gestionmatriculasacademicas.domain.Student;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;

@Mapper(componentModel = "spring", uses = { EnrollmentMapper.class, UserMapper.class })
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {

    @Mapping(target = "student", source = "student", qualifiedByName = "studentId")
    @Mapping(target = "availableSlot", source = "availableSlot", qualifiedByName = "availableSlotId")
    @Mapping(target = "enrollments", source = "enrollments")
    @Mapping(target = "advisor", source = "advisor", qualifiedByName = "login")
    AppointmentDTO toDto(Appointment s);

    @Mapping(target = "removeEnrollment", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "availableSlot", ignore = true)
    @Mapping(target = "advisor", source = "advisor.id", qualifiedByName = "id")
    Appointment toEntity(AppointmentDTO appointmentDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "availableSlot", ignore = true)
    @Mapping(target = "removeEnrollment", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
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
    @Mapping(target = "availableSpots", source = "availableSpots")
    @Mapping(target = "bookedSpots", source = "bookedSpots")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "advisors", source = "advisors", qualifiedByName = "loginSet")
    AvailableSlotDTO toDtoAvailableSlotId(AvailableSlot availableSlot);
}