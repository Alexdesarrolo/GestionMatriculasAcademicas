package com.ale.edu.gestionmatriculasacademicas.service.mapper;

import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.domain.Student;
import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.UserDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {

    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "program", source = "program", qualifiedByName = "programId")
    StudentDTO toDto(Student s);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "program", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "program", ignore = true)
    void partialUpdate(@MappingTarget Student entity, StudentDTO dto);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("programId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "codePrefix", source = "codePrefix")
    ProgramDTO toDtoProgramId(Program program);
}