package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.repository.AvailableSlotRepository;
import com.ale.edu.gestionmatriculasacademicas.service.AvailableSlotService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
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
@RequestMapping("/api/available-slots")
public class AvailableSlotController {

    private static final Logger LOG = LoggerFactory.getLogger(AvailableSlotController.class);

    private final AvailableSlotService availableSlotService;
    private final AvailableSlotRepository availableSlotRepository;

    public AvailableSlotController(
        AvailableSlotService availableSlotService,
        AvailableSlotRepository availableSlotRepository
    ) {
        this.availableSlotService = availableSlotService;
        this.availableSlotRepository = availableSlotRepository;
    }

    // POST /api/available-slots
    @PostMapping
    public ResponseEntity<AvailableSlotDTO> createAvailableSlot(
        @Valid @RequestBody AvailableSlotDTO availableSlotDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AvailableSlot : {}", availableSlotDTO);
        if (availableSlotDTO.getId() != null) {
            throw new BadRequestException("Un nuevo slot no puede tener ID");
        }
        AvailableSlotDTO result = availableSlotService.save(availableSlotDTO);
        return ResponseEntity
            .created(new URI("/api/available-slots/" + result.getId()))
            .body(result);
    }

    // PUT /api/available-slots/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AvailableSlotDTO> updateAvailableSlot(
        @PathVariable Long id,
        @Valid @RequestBody AvailableSlotDTO availableSlotDTO
    ) {
        LOG.debug("REST request to update AvailableSlot : {}", id);
        if (availableSlotDTO.getId() == null || !Objects.equals(id, availableSlotDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!availableSlotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Slot no encontrado con id: " + id);
        }
        AvailableSlotDTO result = availableSlotService.update(availableSlotDTO);
        return ResponseEntity.ok(result);
    }

    // PATCH /api/available-slots/{id}
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AvailableSlotDTO> partialUpdateAvailableSlot(
        @PathVariable Long id,
        @RequestBody AvailableSlotDTO availableSlotDTO
    ) {
        LOG.debug("REST request to partial update AvailableSlot : {}", id);
        if (availableSlotDTO.getId() == null || !Objects.equals(id, availableSlotDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!availableSlotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Slot no encontrado con id: " + id);
        }
        return availableSlotService.partialUpdate(availableSlotDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/available-slots
    @GetMapping
    public ResponseEntity<List<AvailableSlotDTO>> getAllAvailableSlots(Pageable pageable) {
        LOG.debug("REST request to get all AvailableSlots");
        Page<AvailableSlotDTO> page = availableSlotService.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // GET /api/available-slots/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AvailableSlotDTO> getAvailableSlot(@PathVariable Long id) {
        LOG.debug("REST request to get AvailableSlot : {}", id);
        return availableSlotService.findOne(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/available-slots/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailableSlot(@PathVariable Long id) {
        LOG.debug("REST request to delete AvailableSlot : {}", id);
        if (!availableSlotRepository.existsById(id)) {
            throw new ResourceNotFoundException("Slot no encontrado con id: " + id);
        }
        availableSlotService.delete(id);
    }
}