package com.ale.edu.gestionmatriculasacademicas.web.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ale.edu.gestionmatriculasacademicas.repository.StudentRepository;
import com.ale.edu.gestionmatriculasacademicas.service.StudentService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for simple unit tests
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private com.ale.edu.gestionmatriculasacademicas.security.JwtTokenProvider jwtTokenProvider;

    @MockBean
    private com.ale.edu.gestionmatriculasacademicas.security.UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        ProgramDTO programDTO = new ProgramDTO();
        programDTO.setId(1L);

        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setNationalId("12345678");
        studentDTO.setStudentCode("2024-ING-0001");
        studentDTO.setProgram(programDTO);
    }

    @Test
    @WithMockUser
    void createStudent_ShouldReturnCreated() throws Exception {
        studentDTO.setId(null);
        when(studentRepository.existsByNationalId("12345678")).thenReturn(false);
        when(studentService.save(any(StudentDTO.class))).thenAnswer(invocation -> {
            StudentDTO saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser
    void getStudent_ShouldReturnStudent() throws Exception {
        when(studentService.findOne(1L)).thenReturn(Optional.of(studentDTO));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser
    void getStudent_ShouldReturnNotFound() throws Exception {
        when(studentService.findOne(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteStudent_ShouldReturnNoContent() throws Exception {
        when(studentRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}
