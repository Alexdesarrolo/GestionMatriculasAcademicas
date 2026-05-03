package com.ale.edu.gestionmatriculasacademicas.service.impl;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.Enrollment;
import com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering;
import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentStatus;
import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.AvailableSlotRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.EnrollmentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.StudentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.SubjectOfferingRepository;
import com.ale.edu.gestionmatriculasacademicas.service.AppointmentService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.EnrollmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.AppointmentMapper;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final StudentRepository studentRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final SubjectOfferingRepository subjectOfferingRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            AppointmentMapper appointmentMapper,
            StudentRepository studentRepository,
            AvailableSlotRepository availableSlotRepository,
            SubjectOfferingRepository subjectOfferingRepository,
            EnrollmentRepository enrollmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.studentRepository = studentRepository;
        this.availableSlotRepository = availableSlotRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        LOG.debug("Request to save Appointment : {}", appointmentDTO);
        validateAppointment(appointmentDTO);
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        resolveRelations(appointment, appointmentDTO);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    private void validateAppointment(AppointmentDTO appointmentDTO) {
        // Validar que el estudiante no tenga citas activas
        if (appointmentDTO.getStudent() != null && appointmentDTO.getStudent().getId() != null) {
            List<Appointment> activeAppointments = appointmentRepository.findActiveAppointmentsByStudentId(appointmentDTO.getStudent().getId());
            if (!activeAppointments.isEmpty()) {
                throw new BadRequestException("Ya tienes una cita activa. No puedes programar más de una cita a la vez.");
            }
        }

        if (appointmentDTO.getEnrollments() == null || appointmentDTO.getEnrollments().isEmpty()) {
            throw new BadRequestException("Debe seleccionar al menos una materia.");
        }
        if (appointmentDTO.getEnrollments().size() > 7) {
            throw new BadRequestException("No puede matricular más de 7 materias por cita.");
        }

        Set<SubjectOffering> offerings = new HashSet<>();
        for (EnrollmentDTO enrollmentDTO : appointmentDTO.getEnrollments()) {
            if (enrollmentDTO.getSubjectOffering() == null || enrollmentDTO.getSubjectOffering().getId() == null) {
                throw new BadRequestException("Oferta de materia inválida.");
            }
            SubjectOffering offering = subjectOfferingRepository.findById(enrollmentDTO.getSubjectOffering().getId())
                    .orElseThrow(() -> new BadRequestException("Materia no encontrada."));
            
            // Validar capacidad
            long enrolled = enrollmentRepository.countBySubjectOfferingId(offering.getId());
            if (enrolled >= offering.getCapacity()) {
                throw new BadRequestException("La materia " + offering.getSubject().getName() + " ya no tiene cupos disponibles.");
            }

            // Validar choques de horario
            for (SubjectOffering existing : offerings) {
                if (isOverlapping(offering, existing)) {
                    throw new BadRequestException("Conflicto de horario entre " + offering.getSubject().getName() + " y " + existing.getSubject().getName());
                }
            }
            offerings.add(offering);
        }
    }

    private boolean isOverlapping(SubjectOffering s1, SubjectOffering s2) {
        // Solo hay conflicto si es el mismo día de la semana
        if (!s1.getDayOfWeek().equals(s2.getDayOfWeek())) {
            return false;
        }
        return s1.getStartTime().isBefore(s2.getEndTime()) && s2.getStartTime().isBefore(s1.getEndTime());
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) {
        LOG.debug("Request to update Appointment : {}", appointmentDTO);
        validateAppointment(appointmentDTO);
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        resolveRelations(appointment, appointmentDTO);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public Optional<AppointmentDTO> partialUpdate(AppointmentDTO appointmentDTO) {
        LOG.debug("Request to partially update Appointment : {}", appointmentDTO);
        return appointmentRepository.findById(appointmentDTO.getId())
                .map(existingAppointment -> {
                    appointmentMapper.partialUpdate(existingAppointment, appointmentDTO);
                    resolveRelations(existingAppointment, appointmentDTO);
                    return existingAppointment;
                })
                .map(appointmentRepository::save)
                .map(appointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppointmentDTO> findOne(Long id) {
        LOG.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findById(id).map(appointmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findByStudent(Long studentId, Pageable pageable) {
        LOG.debug("Request to get Appointments by student : {}", studentId);
        return appointmentRepository.findByStudentId(studentId, pageable).map(appointmentMapper::toDto);
    }

    @Override
    public Optional<AppointmentDTO> updateStatus(Long id, AppointmentStatus status) {
        LOG.debug("Request to update Appointment status : {} to {}", id, status);
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setStatus(status);
                    appointment.setResponseDate(Instant.now());
                    return appointment;
                })
                .map(appointmentRepository::save)
                .map(appointmentMapper::toDto);
    }

    @Override
    public Optional<AppointmentDTO> cancel(Long id) {
        LOG.debug("Request to cancel Appointment : {}", id);
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
                        throw new BadRequestException("La cita ya está cancelada.");
                    }
                    appointment.setStatus(AppointmentStatus.CANCELLED);
                    appointment.setResponseDate(Instant.now());
                    return appointment;
                })
                .map(appointmentRepository::save)
                .map(appointmentMapper::toDto);
    }

    private void resolveRelations(Appointment appointment, AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getStudent() != null && appointmentDTO.getStudent().getId() != null) {
            studentRepository.findById(appointmentDTO.getStudent().getId())
                    .ifPresent(appointment::setStudent);
        }
        if (appointmentDTO.getAvailableSlot() != null && appointmentDTO.getAvailableSlot().getId() != null) {
            availableSlotRepository.findById(appointmentDTO.getAvailableSlot().getId())
                    .ifPresent(appointment::setAvailableSlot);
        }
        if (appointmentDTO.getEnrollments() != null) {
            Set<Enrollment> enrollments = new HashSet<>();
            for (EnrollmentDTO enrollmentDTO : appointmentDTO.getEnrollments()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentDate(Instant.now());
                enrollment.setStudent(appointment.getStudent());
                enrollment.setAppointment(appointment);
                if (enrollmentDTO.getSubjectOffering() != null && enrollmentDTO.getSubjectOffering().getId() != null) {
                    subjectOfferingRepository.findById(enrollmentDTO.getSubjectOffering().getId())
                            .ifPresent(enrollment::setSubjectOffering);
                }
                enrollments.add(enrollment);
            }
            appointment.setEnrollments(enrollments);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        LOG.debug("Request to get all Appointments with eager relationships");
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toDto);
    }
}
