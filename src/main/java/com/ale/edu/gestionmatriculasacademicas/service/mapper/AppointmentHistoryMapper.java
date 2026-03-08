package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory;
import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentHistoryDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.UserDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentHistoryMapper extends EntityMapper<AppointmentHistoryDTO, AppointmentHistory> {

    @Mapping(target = "appointment", source = "appointment", qualifiedByName = "appointmentId")
    @Mapping(target = "performedBy", source = "performedBy", qualifiedByName = "userLogin")
    AppointmentHistoryDTO toDto(AppointmentHistory s);

    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "performedBy", ignore = true)
    AppointmentHistory toEntity(AppointmentHistoryDTO appointmentHistoryDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "performedBy", ignore = true)
    void partialUpdate(@MappingTarget AppointmentHistory entity, AppointmentHistoryDTO dto);

    @Named("appointmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "currentCredits", source = "currentCredits")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "requestDate", source = "requestDate")
    @Mapping(target = "notes", source = "notes")
    AppointmentDTO toDtoAppointmentId(Appointment appointment);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}