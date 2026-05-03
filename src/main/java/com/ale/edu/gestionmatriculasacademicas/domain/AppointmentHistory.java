package com.ale.edu.gestionmatriculasacademicas.domain;

import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentAction;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppointmentHistory.
 */
@Entity
@Table(name = "appointment_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AppointmentAction action;

    @Column(name = "action_date")
    private Instant actionDate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "student", "availableSlot", "enrollments" }, allowSetters = true)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User performedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppointmentHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppointmentAction getAction() {
        return this.action;
    }

    public AppointmentHistory action(AppointmentAction action) {
        this.setAction(action);
        return this;
    }

    public void setAction(AppointmentAction action) {
        this.action = action;
    }

    public Instant getActionDate() {
        return this.actionDate;
    }

    public AppointmentHistory actionDate(Instant actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public AppointmentHistory notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Appointment getAppointment() {
        return this.appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public AppointmentHistory appointment(Appointment appointment) {
        this.setAppointment(appointment);
        return this;
    }

    public User getPerformedBy() {
        return this.performedBy;
    }

    public void setPerformedBy(User user) {
        this.performedBy = user;
    }

    public AppointmentHistory performedBy(User user) {
        this.setPerformedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointmentHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((AppointmentHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentHistory{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
