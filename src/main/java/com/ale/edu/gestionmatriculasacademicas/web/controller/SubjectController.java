package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.repository.SubjectRepository;
import com.ale.edu.gestionmatriculasacademicas.service.SubjectService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectDTO;
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
@RequestMapping("/api/subjects")
public class SubjectController {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectController.class);

    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectService subjectService, SubjectRepository subjectRepository) {
        this.subjectService = subjectService;
        this.subjectRepository = subjectRepository;
    }

    // POST /api/subjects
    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody SubjectDTO subjectDTO) throws URISyntaxException {
        LOG.debug("REST request to save Subject : {}", subjectDTO);
        if (subjectDTO.getId() != null) {
            throw new BadRequestException("Un nuevo subject no puede tener ID");
        }
        SubjectDTO result = subjectService.save(subjectDTO);
        return ResponseEntity
            .created(new URI("/api/subjects/" + result.getId()))
            .body(result);
    }

    // PUT /api/subjects/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> updateSubject(
        @PathVariable Long id,
        @Valid @RequestBody SubjectDTO subjectDTO
    ) {
        LOG.debug("REST request to update Subject : {}", id);
        if (subjectDTO.getId() == null || !Objects.equals(id, subjectDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject no encontrado con id: " + id);
        }
        SubjectDTO result = subjectService.update(subjectDTO);
        return ResponseEntity.ok(result);
    }

    // PATCH /api/subjects/{id}
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubjectDTO> partialUpdateSubject(
        @PathVariable Long id,
        @RequestBody SubjectDTO subjectDTO
    ) {
        LOG.debug("REST request to partial update Subject : {}", id);
        if (subjectDTO.getId() == null || !Objects.equals(id, subjectDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject no encontrado con id: " + id);
        }
        return subjectService.partialUpdate(subjectDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/subjects
    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects(Pageable pageable) {
        LOG.debug("REST request to get all Subjects");
        Page<SubjectDTO> page = subjectService.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // GET /api/subjects/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubject(@PathVariable Long id) {
        LOG.debug("REST request to get Subject : {}", id);
        return subjectService.findOne(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/subjects/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable Long id) {
        LOG.debug("REST request to delete Subject : {}", id);
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject no encontrado con id: " + id);
        }
        subjectService.delete(id);
    }
}