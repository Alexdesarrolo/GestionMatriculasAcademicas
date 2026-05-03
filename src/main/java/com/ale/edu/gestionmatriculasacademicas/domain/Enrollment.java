package com.ale.edu.gestionmatriculasacademicas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enrollment.
 */
@Entity
@Table(name = "enrollment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "enrollment_date", nullable = false)
    private Instant enrollmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "program" }, allowSetters = true)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectOffering subjectOffering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "student", "availableSlot", "enrollments" }, allowSetters = true)
    private Appointment appointment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Instant enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SubjectOffering getSubjectOffering() {
        return subjectOffering;
    }

    public void setSubjectOffering(SubjectOffering subjectOffering) {
        this.subjectOffering = subjectOffering;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        return id != null && id.equals(((Enrollment) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Enrollment{" +
            "id=" + id +
            ", enrollmentDate='" + enrollmentDate + "'" +
            "}";
    }
}
