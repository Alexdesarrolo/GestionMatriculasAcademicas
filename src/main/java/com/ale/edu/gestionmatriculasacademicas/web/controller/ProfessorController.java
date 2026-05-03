package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.repository.ProfessorRepository;
import com.ale.edu.gestionmatriculasacademicas.service.ProfessorService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProfessorDTO;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.ResourceNotFoundException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.Professor}.
 */
@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessorController.class);

    private final ProfessorService professorService;

    private final ProfessorRepository professorRepository;

    public ProfessorController(ProfessorService professorService, ProfessorRepository professorRepository) {
        this.professorService = professorService;
        this.professorRepository = professorRepository;
    }

    /**
     * {@code POST  /professors} : Create a new professor.
     *
     * @param professorDTO the professorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professorDTO, or with status {@code 400 (Bad Request)} if the professor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<ProfessorDTO> createProfessor(@Valid @RequestBody ProfessorDTO professorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Professor : {}", professorDTO);
        if (professorDTO.getId() != null) {
            throw new BadRequestException("A new professor cannot already have an ID");
        }
        ProfessorDTO result = professorService.save(professorDTO);
        return ResponseEntity
            .created(new URI("/api/professors/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /professors/:id} : Updates an existing professor.
     *
     * @param id the id of the professorDTO to save.
     * @param professorDTO the professorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professorDTO,
     * or with status {@code 400 (Bad Request)} if the professorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professorDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> updateProfessor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessorDTO professorDTO
    ) {
        LOG.debug("REST request to update Professor : {}, {}", id, professorDTO);
        if (professorDTO.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, professorDTO.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!professorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entity not found");
        }

        ProfessorDTO result = professorService.update(professorDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /professors/:id} : Partial updates given fields of an existing professor, field will ignore if it is null
     *
     * @param id the id of the professorDTO to save.
     * @param professorDTO the professorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professorDTO,
     * or with status {@code 400 (Bad Request)} if the professorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professorDTO couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessorDTO> partialUpdateProfessor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessorDTO professorDTO
    ) {
        LOG.debug("REST request to partial update Professor partially : {}, {}", id, professorDTO);
        if (professorDTO.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, professorDTO.getId())) {
            throw new BadRequestException("Invalid id");
        }

        if (!professorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entity not found");
        }

        return professorService
            .partialUpdate(professorDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code GET  /professors} : get all the professors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professors in body.
     */
    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors(Pageable pageable) {
        LOG.debug("REST request to get all Professors");
        Page<ProfessorDTO> page = professorService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    /**
     * {@code GET  /professors/:id} : get the "id" professor.
     *
     * @param id the id of the professorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getProfessor(@PathVariable Long id) {
        LOG.debug("REST request to get Professor : {}", id);
        return professorService.findOne(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /professors/:id} : delete the "id" professor.
     *
     * @param id the id of the professorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        LOG.debug("REST request to delete Professor : {}", id);
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
