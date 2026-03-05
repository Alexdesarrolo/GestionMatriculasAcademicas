package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.repository.ProgramRepository;
import com.ale.edu.gestionmatriculasacademicas.service.ProgramService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
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
@RequestMapping("/api/programs")
public class ProgramResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramResource.class);

    private final ProgramService programService;
    private final ProgramRepository programRepository;

    public ProgramResource(ProgramService programService, ProgramRepository programRepository) {
        this.programService = programService;
        this.programRepository = programRepository;
    }

    // POST /api/programs
    @PostMapping
    public ResponseEntity<ProgramDTO> createProgram(@Valid @RequestBody ProgramDTO programDTO) throws URISyntaxException {
        LOG.debug("REST request to save Program : {}", programDTO);
        if (programDTO.getId() != null) {
            throw new BadRequestException("Un nuevo programa no puede tener ID");
        }
        ProgramDTO result = programService.save(programDTO);
        return ResponseEntity
            .created(new URI("/api/programs/" + result.getId()))
            .body(result);
    }

    // PUT /api/programs/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProgramDTO> updateProgram(
        @PathVariable Long id,
        @Valid @RequestBody ProgramDTO programDTO
    ) {
        LOG.debug("REST request to update Program : {}", id);
        if (programDTO.getId() == null || !Objects.equals(id, programDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!programRepository.existsById(id)) {
            throw new ResourceNotFoundException("Programa no encontrado con id: " + id);
        }
        ProgramDTO result = programService.update(programDTO);
        return ResponseEntity.ok(result);
    }

    // PATCH /api/programs/{id}
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProgramDTO> partialUpdateProgram(
        @PathVariable Long id,
        @RequestBody ProgramDTO programDTO
    ) {
        LOG.debug("REST request to partial update Program : {}", id);
        if (programDTO.getId() == null || !Objects.equals(id, programDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!programRepository.existsById(id)) {
            throw new ResourceNotFoundException("Programa no encontrado con id: " + id);
        }
        return programService.partialUpdate(programDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/programs
    @GetMapping
    public ResponseEntity<List<ProgramDTO>> getAllPrograms(Pageable pageable) {
        LOG.debug("REST request to get all Programs");
        Page<ProgramDTO> page = programService.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // GET /api/programs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProgramDTO> getProgram(@PathVariable Long id) {
        LOG.debug("REST request to get Program : {}", id);
        return programService.findOne(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/programs/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProgram(@PathVariable Long id) {
        LOG.debug("REST request to delete Program : {}", id);
        if (!programRepository.existsById(id)) {
            throw new ResourceNotFoundException("Programa no encontrado con id: " + id);
        }
        programService.delete(id);
    }
}