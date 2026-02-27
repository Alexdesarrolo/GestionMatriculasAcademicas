package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.domain.Subject;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subject} and its DTO {@link SubjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {
    @Mapping(target = "programs", source = "programs", qualifiedByName = "programNameSet")
    @Mapping(target = "appointments", source = "appointments", qualifiedByName = "appointmentIdSet")
    SubjectDTO toDto(Subject s);

    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "removePrograms", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    @Mapping(target = "removeAppointments", ignore = true)
    Subject toEntity(SubjectDTO subjectDTO);

    @Named("programName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProgramDTO toDtoProgramName(Program program);

    @Named("programNameSet")
    default Set<ProgramDTO> toDtoProgramNameSet(Set<Program> program) {
        return program.stream().map(this::toDtoProgramName).collect(Collectors.toSet());
    }

    @Named("appointmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppointmentDTO toDtoAppointmentId(Appointment appointment);

    @Named("appointmentIdSet")
    default Set<AppointmentDTO> toDtoAppointmentIdSet(Set<Appointment> appointment) {
        return appointment.stream().map(this::toDtoAppointmentId).collect(Collectors.toSet());
    }
}
