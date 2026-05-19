package com.ale.edu.gestionmatriculasacademicas.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.Subject;
import com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering;
import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentStatus;
import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.DayOfWeek;
import com.ale.edu.gestionmatriculasacademicas.repository.*;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.EnrollmentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectOfferingDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.AppointmentMapper;
import com.ale.edu.gestionmatriculasacademicas.web.controller.errors.BadRequestException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private AvailableSlotRepository availableSlotRepository;
    @Mock
    private SubjectOfferingRepository subjectOfferingRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentDTO appointmentDTO;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        studentDTO = new StudentDTO();
        studentDTO.setId(1L);

        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setStudent(studentDTO);
        
        Set<EnrollmentDTO> enrollments = new HashSet<>();
        EnrollmentDTO enrollment1 = new EnrollmentDTO();
        SubjectOfferingDTO offeringDTO1 = new SubjectOfferingDTO();
        offeringDTO1.setId(1L);
        enrollment1.setSubjectOffering(offeringDTO1);
        enrollments.add(enrollment1);
        
        appointmentDTO.setEnrollments(enrollments);
    }

    @Test
    void save_ShouldThrowException_WhenStudentHasActiveAppointment() {
        // Arrange
        when(appointmentRepository.findByStudentIdAndStatusIn(anyLong(), anyList()))
            .thenReturn(Collections.singletonList(new Appointment()));

        // Act & Assert
        assertThatThrownBy(() -> appointmentService.save(appointmentDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Ya tienes una cita activa");
    }

    @Test
    void save_ShouldThrowException_WhenTooManySubjects() {
        // Arrange
        when(appointmentRepository.findByStudentIdAndStatusIn(anyLong(), anyList())).thenReturn(new ArrayList<>());
        
        Set<EnrollmentDTO> manyEnrollments = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            EnrollmentDTO e = new EnrollmentDTO();
            e.setId((long) i); // Unique ID to avoid being seen as same in Set
            SubjectOfferingDTO s = new SubjectOfferingDTO();
            s.setId((long) i);
            e.setSubjectOffering(s);
            manyEnrollments.add(e);
        }
        appointmentDTO.setEnrollments(manyEnrollments);

        // Act & Assert
        assertThatThrownBy(() -> appointmentService.save(appointmentDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("No puede matricular más de 7 materias");
    }

    @Test
    void save_ShouldThrowException_WhenScheduleConflict() {
        // Arrange
        when(appointmentRepository.findByStudentIdAndStatusIn(anyLong(), anyList())).thenReturn(new ArrayList<>());

        SubjectOffering offering1 = createOffering(1L, "Math", DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0), 30);
        SubjectOffering offering2 = createOffering(2L, "Physics", DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0), 30);

        when(subjectOfferingRepository.findById(1L)).thenReturn(Optional.of(offering1));
        when(subjectOfferingRepository.findById(2L)).thenReturn(Optional.of(offering2));
        when(enrollmentRepository.countBySubjectOfferingId(anyLong())).thenReturn(0L);

        Set<EnrollmentDTO> enrollments = new HashSet<>();
        enrollments.add(createEnrollmentDTO(1L, 1L));
        enrollments.add(createEnrollmentDTO(2L, 2L));
        appointmentDTO.setEnrollments(enrollments);

        // Act & Assert
        assertThatThrownBy(() -> appointmentService.save(appointmentDTO))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("Conflicto de horario");
    }

    @Test
    void updateStatus_ShouldUpdateStatusAndResponseDate() {
        // Arrange
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(AppointmentStatus.PENDING);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(new AppointmentDTO());

        // Act
        Optional<AppointmentDTO> result = appointmentService.updateStatus(1L, AppointmentStatus.APPROVED);

        // Assert
        assertThat(result).isPresent();
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.APPROVED);
        assertThat(appointment.getResponseDate()).isNotNull();
        verify(appointmentRepository).save(appointment);
    }

    private SubjectOffering createOffering(Long id, String name, DayOfWeek day, LocalTime start, LocalTime end, int capacity) {
        SubjectOffering offering = new SubjectOffering();
        offering.setId(id);
        offering.setDayOfWeek(day);
        offering.setStartTime(start);
        offering.setEndTime(end);
        offering.setCapacity(capacity);
        
        Subject subject = new Subject();
        subject.setName(name);
        offering.setSubject(subject);
        
        return offering;
    }

    private EnrollmentDTO createEnrollmentDTO(Long enrollmentId, Long offeringId) {
        EnrollmentDTO e = new EnrollmentDTO();
        e.setId(enrollmentId);
        SubjectOfferingDTO s = new SubjectOfferingDTO();
        s.setId(offeringId);
        e.setSubjectOffering(s);
        return e;
    }
}
