package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.repository.StudentRepository;
import com.ale.edu.gestionmatriculasacademicas.service.StudentService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.ResourceNotFoundException;

import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    // POST /api/students
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Student : {}", studentDTO);
        if (studentDTO.getId() != null) {
            throw new BadRequestException("Un nuevo estudiante no puede tener ID");
        }
        StudentDTO result = studentService.save(studentDTO);
        return ResponseEntity
            .created(new URI("/api/students/" + result.getId()))
            .body(result);
    }

    // PUT /api/students/{id}
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
        @PathVariable Long id,
        @Valid @RequestBody StudentDTO studentDTO
    ) {
        LOG.debug("REST request to update Student : {}", id);
        if (studentDTO.getId() == null || !Objects.equals(id, studentDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id: " + id);
        }
        StudentDTO result = studentService.update(studentDTO);
        return ResponseEntity.ok(result);
    }

    // PATCH /api/students/{id}
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentDTO> partialUpdateStudent(
        @PathVariable Long id,
        @RequestBody StudentDTO studentDTO
    ) {
        LOG.debug("REST request to partial update Student : {}", id);
        if (studentDTO.getId() == null || !Objects.equals(id, studentDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id: " + id);
        }
        return studentService.partialUpdate(studentDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/students
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents(Pageable pageable) {
        LOG.debug("REST request to get all Students");
        Page<StudentDTO> page = studentService.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // GET /api/students/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        LOG.debug("REST request to get Student : {}", id);
        return studentService.findOne(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/students/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        LOG.debug("REST request to delete Student : {}", id);
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id: " + id);
        }
        studentService.delete(id);
    }
}