package com.ale.edu.gestionmatriculasacademicas.domain;

import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "current_credits", nullable = false)
    private Integer currentCredits;

    @Lob
    @Column(name = "current_schedule")
    private String currentSchedule;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "request_date")
    private Instant requestDate;

    @Column(name = "response_date")
    private Instant responseDate;

    @Column(name = "notes")
    private String notes;

   @ManyToOne(fetch = FetchType.EAGER)
@JsonIgnoreProperties(value = { "user", "program" }, allowSetters = true)
private Student student;

@ManyToOne(fetch = FetchType.EAGER)
@JsonIgnoreProperties(value = { "program" }, allowSetters = true)
private AvailableSlot availableSlot;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_appointment__desired_subjects",
        joinColumns = @JoinColumn(name = "appointment_id"),
        inverseJoinColumns = @JoinColumn(name = "desired_subjects_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "programs", "appointments" }, allowSetters = true)
    private Set<Subject> desiredSubjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Appointment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrentCredits() {
        return this.currentCredits;
    }

    public Appointment currentCredits(Integer currentCredits) {
        this.setCurrentCredits(currentCredits);
        return this;
    }

    public void setCurrentCredits(Integer currentCredits) {
        this.currentCredits = currentCredits;
    }

    public String getCurrentSchedule() {
        return this.currentSchedule;
    }

    public Appointment currentSchedule(String currentSchedule) {
        this.setCurrentSchedule(currentSchedule);
        return this;
    }

    public void setCurrentSchedule(String currentSchedule) {
        this.currentSchedule = currentSchedule;
    }

    public AppointmentStatus getStatus() {
        return this.status;
    }

    public Appointment status(AppointmentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Instant getRequestDate() {
        return this.requestDate;
    }

    public Appointment requestDate(Instant requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public Instant getResponseDate() {
        return this.responseDate;
    }

    public Appointment responseDate(Instant responseDate) {
        this.setResponseDate(responseDate);
        return this;
    }

    public void setResponseDate(Instant responseDate) {
        this.responseDate = responseDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public Appointment notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Appointment student(Student student) {
        this.setStudent(student);
        return this;
    }

    public AvailableSlot getAvailableSlot() {
        return this.availableSlot;
    }

    public void setAvailableSlot(AvailableSlot availableSlot) {
        this.availableSlot = availableSlot;
    }

    public Appointment availableSlot(AvailableSlot availableSlot) {
        this.setAvailableSlot(availableSlot);
        return this;
    }

    public Set<Subject> getDesiredSubjects() {
        return this.desiredSubjects;
    }

    public void setDesiredSubjects(Set<Subject> subjects) {
        this.desiredSubjects = subjects;
    }

    public Appointment desiredSubjects(Set<Subject> subjects) {
        this.setDesiredSubjects(subjects);
        return this;
    }

    public Appointment addDesiredSubjects(Subject subject) {
        this.desiredSubjects.add(subject);
        return this;
    }

    public Appointment removeDesiredSubjects(Subject subject) {
        this.desiredSubjects.remove(subject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointment)) {
            return false;
        }
        return getId() != null && getId().equals(((Appointment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + getId() +
            ", currentCredits=" + getCurrentCredits() +
            ", currentSchedule='" + getCurrentSchedule() + "'" +
            ", status='" + getStatus() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
