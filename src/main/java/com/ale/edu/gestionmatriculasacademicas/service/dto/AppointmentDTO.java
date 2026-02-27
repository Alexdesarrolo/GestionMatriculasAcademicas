package com.ale.edu.gestionmatriculasacademicas.service.dto;

import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.ale.edu.gestionmatriculasacademicas.domain.Appointment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer currentCredits;

    @Lob
    private String currentSchedule;

    @NotNull
    private AppointmentStatus status;

    private Instant requestDate;

    private Instant responseDate;

    private String notes;

    private StudentDTO student;

    private AvailableSlotDTO availableSlot;

    private Set<SubjectDTO> desiredSubjects = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrentCredits() {
        return currentCredits;
    }

    public void setCurrentCredits(Integer currentCredits) {
        this.currentCredits = currentCredits;
    }

    public String getCurrentSchedule() {
        return currentSchedule;
    }

    public void setCurrentSchedule(String currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Instant getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public Instant getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Instant responseDate) {
        this.responseDate = responseDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public AvailableSlotDTO getAvailableSlot() {
        return availableSlot;
    }

    public void setAvailableSlot(AvailableSlotDTO availableSlot) {
        this.availableSlot = availableSlot;
    }

    public Set<SubjectDTO> getDesiredSubjects() {
        return desiredSubjects;
    }

    public void setDesiredSubjects(Set<SubjectDTO> desiredSubjects) {
        this.desiredSubjects = desiredSubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentDTO)) {
            return false;
        }

        AppointmentDTO appointmentDTO = (AppointmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + getId() +
            ", currentCredits=" + getCurrentCredits() +
            ", currentSchedule='" + getCurrentSchedule() + "'" +
            ", status='" + getStatus() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", student=" + getStudent() +
            ", availableSlot=" + getAvailableSlot() +
            ", desiredSubjects=" + getDesiredSubjects() +
            "}";
    }
}
