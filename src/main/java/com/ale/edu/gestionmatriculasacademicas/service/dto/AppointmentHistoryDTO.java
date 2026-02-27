package com.ale.edu.gestionmatriculasacademicas.service.dto;

import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentAction;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private AppointmentAction action;

    private Instant actionDate;

    private String notes;

    private AppointmentDTO appointment;

    private UserDTO performedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppointmentAction getAction() {
        return action;
    }

    public void setAction(AppointmentAction action) {
        this.action = action;
    }

    public Instant getActionDate() {
        return actionDate;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AppointmentDTO getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentDTO appointment) {
        this.appointment = appointment;
    }

    public UserDTO getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(UserDTO performedBy) {
        this.performedBy = performedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentHistoryDTO)) {
            return false;
        }

        AppointmentHistoryDTO appointmentHistoryDTO = (AppointmentHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointmentHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentHistoryDTO{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", appointment=" + getAppointment() +
            ", performedBy=" + getPerformedBy() +
            "}";
    }
}
