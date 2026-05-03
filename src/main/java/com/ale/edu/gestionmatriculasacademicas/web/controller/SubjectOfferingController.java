package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.service.SubjectOfferingService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectOfferingDTO;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering}.
 */
@RestController
@RequestMapping("/api/subject-offerings")
public class SubjectOfferingController {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectOfferingController.class);

    private final SubjectOfferingService subjectOfferingService;

    public SubjectOfferingController(SubjectOfferingService subjectOfferingService) {
        this.subjectOfferingService = subjectOfferingService;
    }

    @PostMapping
    public ResponseEntity<SubjectOfferingDTO> createSubjectOffering(@Valid @RequestBody SubjectOfferingDTO subjectOfferingDTO) throws URISyntaxException {
        LOG.debug("REST request to save SubjectOffering : {}", subjectOfferingDTO);
        if (subjectOfferingDTO.getId() != null) {
            throw new BadRequestException("A new subjectOffering cannot already have an ID");
        }
        SubjectOfferingDTO result = subjectOfferingService.save(subjectOfferingDTO);
        return ResponseEntity.created(new URI("/api/subject-offerings/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectOfferingDTO> updateSubjectOffering(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubjectOfferingDTO subjectOfferingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubjectOffering : {}, {}", id, subjectOfferingDTO);
        if (subjectOfferingDTO.getId() == null) {
            throw new BadRequestException("Invalid id");
        }
        if (!Objects.equals(id, subjectOfferingDTO.getId())) {
            throw new BadRequestException("Invalid id");
        }
        SubjectOfferingDTO result = subjectOfferingService.update(subjectOfferingDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<List<SubjectOfferingDTO>> getAllSubjectOfferings(Pageable pageable) {
        LOG.debug("REST request to get all SubjectOfferings");
        Page<SubjectOfferingDTO> page = subjectOfferingService.findAll(pageable);
        return ResponseEntity.ok().body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectOfferingDTO> getSubjectOffering(@PathVariable Long id) {
        LOG.debug("REST request to get SubjectOffering : {}", id);
        return subjectOfferingService.findOne(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectOffering(@PathVariable Long id) {
        LOG.debug("REST request to delete SubjectOffering : {}", id);
        subjectOfferingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
