package com.ale.edu.gestionmatriculasacademicas.web.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentRepository;
import com.ale.edu.gestionmatriculasacademicas.service.AppointmentService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.ResourceNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;

    public AppointmentController(
        AppointmentService appointmentService,
        AppointmentRepository appointmentRepository
    ) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
    }

    // POST /api/appointments
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(
        @Valid @RequestBody AppointmentDTO appointmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save Appointment : {}", appointmentDTO);
        if (appointmentDTO.getId() != null) {
            throw new BadRequestException("Una nueva cita no puede tener ID");
        }
        AppointmentDTO result = appointmentService.save(appointmentDTO);
        return ResponseEntity
            .created(new URI("/api/appointments/" + result.getId()))
            .body(result);
    }

    // PUT /api/appointments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(
        @PathVariable Long id,
        @Valid @RequestBody AppointmentDTO appointmentDTO
    ) {
        LOG.debug("REST request to update Appointment : {}", id);
        if (appointmentDTO.getId() == null || !Objects.equals(id, appointmentDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con id: " + id);
        }
        AppointmentDTO result = appointmentService.update(appointmentDTO);
        return ResponseEntity.ok(result);
    }

    // PATCH /api/appointments/{id}
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppointmentDTO> partialUpdateAppointment(
        @PathVariable Long id,
        @RequestBody AppointmentDTO appointmentDTO
    ) {
        LOG.debug("REST request to partial update Appointment : {}", id);
        if (appointmentDTO.getId() == null || !Objects.equals(id, appointmentDTO.getId())) {
            throw new BadRequestException("ID inválido");
        }
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con id: " + id);
        }
        return appointmentService.partialUpdate(appointmentDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/appointments
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(Pageable pageable) {
        LOG.debug("REST request to get all Appointments");
        Page<AppointmentDTO> page = appointmentService.findAll(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // GET /api/appointments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable Long id) {
        LOG.debug("REST request to get Appointment : {}", id);
        return appointmentService.findOne(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/appointments/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByStudent(
        @PathVariable Long studentId,
        Pageable pageable
    ) {
        LOG.debug("REST request to get Appointments by student : {}", studentId);
        Page<AppointmentDTO> page = appointmentService.findByStudent(studentId, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // PATCH /api/appointments/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(
        @PathVariable Long id,
        @RequestBody AppointmentDTO appointmentDTO
    ) {
        LOG.debug("REST request to update Appointment status : {}", id);
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con id: " + id);
        }
        appointmentDTO.setId(id);
        return appointmentService.updateStatus(id, appointmentDTO.getStatus())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/appointments/{id}/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id) {
        LOG.debug("REST request to cancel Appointment : {}", id);
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con id: " + id);
        }
        return appointmentService.cancel(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/appointments/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppointment(@PathVariable Long id) {
        LOG.debug("REST request to delete Appointment : {}", id);
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada con id: " + id);
        }
        appointmentService.delete(id);
    }
}