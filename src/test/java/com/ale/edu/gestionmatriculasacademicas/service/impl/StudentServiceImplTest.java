package com.ale.edu.gestionmatriculasacademicas.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.domain.Student;
import com.ale.edu.gestionmatriculasacademicas.domain.User;
import com.ale.edu.gestionmatriculasacademicas.repository.ProgramRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.StudentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.UserRepository;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.UserDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.StudentMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDTO studentDTO;
    private Program program;
    private ProgramDTO programDTO;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        program = new Program();
        program.setId(1L);
        program.setCodePrefix("ING");

        programDTO = new ProgramDTO();
        programDTO.setId(1L);

        user = new User();
        user.setId(2L);
        user.setLogin("testuser");

        userDTO = new UserDTO();
        userDTO.setId(2L);

        student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setNationalId("12345678");

        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setNationalId("12345678");
        studentDTO.setProgram(programDTO);
        studentDTO.setUser(userDTO);
    }

    @Test
    void save_ShouldGenerateStudentCodeAndSave() {
        // Arrange
        student.setId(null);
        studentDTO.setId(null);
        
        when(studentMapper.toEntity(any(StudentDTO.class))).thenReturn(student);
        when(programRepository.findById(1L)).thenReturn(Optional.of(program));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(studentRepository.countByStudentCodeStartingWith(anyString())).thenReturn(5L);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.toDto(any(Student.class))).thenReturn(studentDTO);

        // Act
        StudentDTO result = studentService.save(studentDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(student.getStudentCode()).startsWith(java.time.Year.now().getValue() + "-ING-");
        assertThat(student.getStudentCode()).endsWith("0006");
        verify(studentRepository).save(student);
    }

    @Test
    void findOne_ShouldReturnStudent() {
        // Arrange
        when(studentRepository.findOneWithEagerRelationships(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDTO);

        // Act
        Optional<StudentDTO> result = studentService.findOne(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
        verify(studentRepository).findOneWithEagerRelationships(1L);
    }

    @Test
    void delete_ShouldCallRepository() {
        // Act
        studentService.delete(1L);

        // Assert
        verify(studentRepository).deleteById(1L);
    }
}
