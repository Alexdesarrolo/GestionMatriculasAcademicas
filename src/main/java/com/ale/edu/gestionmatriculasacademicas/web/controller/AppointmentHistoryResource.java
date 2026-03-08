package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentHistoryRepository;
import com.ale.edu.gestionmatriculasacademicas.service.AppointmentHistoryService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentHistoryDTO;
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
@RequestMapping("/api/appointment-histories")
public class AppointmentHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentHistoryResource.class);

    private final AppointmentHistoryService appointmentHistoryService;
    private final AppointmentHistoryRepository appointmentHistoryRepository;

    public AppointmentHistoryResource(
        AppointmentHistoryService appointmentHistoryService,
        AppointmentHistoryRepository appointmentHistoryRepository
    ) {
        this.appointmentHistoryService = appointmentHistoryService;
        this.appointmentHistoryRepository = appointmentHistoryRepository;
    }

    // POST /api/appointment-histories
    @PostMapping
    public ResponseEntity<AppointmentHistoryDTO> createAppointmentHistory(
        @Valid @RequestBody AppointmentHistoryDTO appointmentHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AppointmentHistory : {}", appointmentHistoryDTO);
        if (appointmentHistoryDTO.getId() != null) {
            throw new BadRequestException("Un nuevo historial no puede tener ID");
        }
        AppointmentHistoryDTO result = appointmentHistoryService.save(appointmentHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/appointment-histories/" + result.getId()))
            .body(result);
    }

    // PUT /api/appointment-histories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentHistoryDTO> updateAppointmentHistory(
        @PathVariable Long id,
        @Valid @RequestBody AppointmentHistoryDTO appointmentHistoryDTO
    ) {
        LOG.debug("REST request to update AppointmentHistory : {}", id);
        if (appointmentHistoryDTO.getId() == null || !Objects.equals(id, appointmentHistoryDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!appointmentHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historial no encontrado con id: " + id);
        }
        AppointmentHistoryDTO result = appointmentHistoryService.update(appointmentHistoryDTO);
        return ResponseEntity.ok(result);
    }

    // PATCH /api/appointment-histories/{id}
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppointmentHistoryDTO> partialUpdateAppointmentHistory(
        @PathVariable Long id,
        @RequestBody AppointmentHistoryDTO appointmentHistoryDTO
    ) {
        LOG.debug("REST request to partial update AppointmentHistory : {}", id);
        if (appointmentHistoryDTO.getId() == null || !Objects.equals(id, appointmentHistoryDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!appointmentHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historial no encontrado con id: " + id);
        }
        return appointmentHistoryService.partialUpdate(appointmentHistoryDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/appointment-histories
    @GetMapping
    public ResponseEntity<List<AppointmentHistoryDTO>> getAllAppointmentHistories(Pageable pageable) {
        LOG.debug("REST request to get all AppointmentHistories");
        Page<AppointmentHistoryDTO> page = appointmentHistoryService.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // GET /api/appointment-histories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentHistoryDTO> getAppointmentHistory(@PathVariable Long id) {
        LOG.debug("REST request to get AppointmentHistory : {}", id);
        return appointmentHistoryService.findOne(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/appointment-histories/appointment/{appointmentId}
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<AppointmentHistoryDTO>> getHistoryByAppointment(
        @PathVariable Long appointmentId,
        Pageable pageable
    ) {
        LOG.debug("REST request to get AppointmentHistory by appointment : {}", appointmentId);
        Page<AppointmentHistoryDTO> page = appointmentHistoryService.findByAppointment(appointmentId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // DELETE /api/appointment-histories/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointmentHistory(@PathVariable Long id) {
        LOG.debug("REST request to delete AppointmentHistory : {}", id);
        if (!appointmentHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historial no encontrado con id: " + id);
        }
        appointmentHistoryService.delete(id);
    }
}