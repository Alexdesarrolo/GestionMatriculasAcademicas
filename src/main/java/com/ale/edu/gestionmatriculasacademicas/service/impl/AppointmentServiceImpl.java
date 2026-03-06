package com.ale.edu.gestionmatriculasacademicas.service.impl;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.Subject;
import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentStatus;
import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.AvailableSlotRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.StudentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.SubjectRepository;
import com.ale.edu.gestionmatriculasacademicas.service.AppointmentService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.AppointmentMapper;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final StudentRepository studentRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final SubjectRepository subjectRepository;

    public AppointmentServiceImpl(
        AppointmentRepository appointmentRepository,
        AppointmentMapper appointmentMapper,
        StudentRepository studentRepository,
        AvailableSlotRepository availableSlotRepository,
        SubjectRepository subjectRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.studentRepository = studentRepository;
        this.availableSlotRepository = availableSlotRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        LOG.debug("Request to save Appointment : {}", appointmentDTO);
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        resolveRelations(appointment, appointmentDTO);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) {
        LOG.debug("Request to update Appointment : {}", appointmentDTO);
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
        return appointmentRepository.findOneWithEagerRelationships(id).map(appointmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
    }

    // Consultar citas por estudiante
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findByStudent(Long studentId, Pageable pageable) {
        LOG.debug("Request to get Appointments by student : {}", studentId);
        return appointmentRepository.findByStudentId(studentId, pageable).map(appointmentMapper::toDto);
    }

    // Aprobar o rechazar cita
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

    // Resuelve student, availableSlot y desiredSubjects
    private void resolveRelations(Appointment appointment, AppointmentDTO appointmentDTO) {
        if (appointmentDTO.getStudent() != null && appointmentDTO.getStudent().getId() != null) {
            studentRepository.findById(appointmentDTO.getStudent().getId())
                .ifPresent(appointment::setStudent);
        }
        if (appointmentDTO.getAvailableSlot() != null && appointmentDTO.getAvailableSlot().getId() != null) {
            availableSlotRepository.findById(appointmentDTO.getAvailableSlot().getId())
                .ifPresent(appointment::setAvailableSlot);
        }
        if (appointmentDTO.getDesiredSubjects() != null) {
            Set<Subject> subjects = new HashSet<>();
            for (SubjectDTO subjectDTO : appointmentDTO.getDesiredSubjects()) {
                if (subjectDTO.getId() != null) {
                    subjectRepository.findById(subjectDTO.getId())
                        .ifPresent(subjects::add);
                }
            }
            appointment.setDesiredSubjects(subjects);
        }
    }

    @Override
    public Page<AppointmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllWithEagerRelationships'");
    }
}