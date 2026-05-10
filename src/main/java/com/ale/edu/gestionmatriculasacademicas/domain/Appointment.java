package com.ale.edu.gestionmatriculasacademicas.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appointment", "student", "subjectOffering" }, allowSetters = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "authorities", "imageUrl" }, allowSetters = true)
    private User advisor;

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

    public Set<Enrollment> getEnrollments() {
        return this.enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        if (this.enrollments != null) {
            this.enrollments.forEach(i -> i.setAppointment(null));
        }
        if (enrollments != null) {
            enrollments.forEach(i -> i.setAppointment(this));
        }
        this.enrollments = enrollments;
    }

    public Appointment enrollments(Set<Enrollment> enrollments) {
        this.setEnrollments(enrollments);
        return this;
    }

    public Appointment addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setAppointment(this);
        return this;
    }

    public Appointment removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.setAppointment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public User getAdvisor() {
        return this.advisor;
    }

    public void setAdvisor(User advisor) {
        this.advisor = advisor;
    }

    public Appointment advisor(User advisor) {
        this.setAdvisor(advisor);
        return this;
    }

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
