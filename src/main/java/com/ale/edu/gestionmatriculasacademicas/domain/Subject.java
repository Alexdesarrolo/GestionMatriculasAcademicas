package com.ale.edu.gestionmatriculasacademicas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "credits")
    private Integer credits;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subjects" }, allowSetters = true)
    private Set<Program> programs = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "desiredSubjects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "student", "availableSlot", "desiredSubjects" }, allowSetters = true)
    private Set<Appointment> appointments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Subject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Subject name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Subject code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCredits() {
        return this.credits;
    }

    public Subject credits(Integer credits) {
        this.setCredits(credits);
        return this;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Set<Program> getPrograms() {
        return this.programs;
    }

    public void setPrograms(Set<Program> programs) {
        if (this.programs != null) {
            this.programs.forEach(i -> i.removeSubject(this));
        }
        if (programs != null) {
            programs.forEach(i -> i.addSubject(this));
        }
        this.programs = programs;
    }

    public Subject programs(Set<Program> programs) {
        this.setPrograms(programs);
        return this;
    }

    public Subject addPrograms(Program program) {
        this.programs.add(program);
        program.getSubjects().add(this);
        return this;
    }

    public Subject removePrograms(Program program) {
        this.programs.remove(program);
        program.getSubjects().remove(this);
        return this;
    }

    public Set<Appointment> getAppointments() {
        return this.appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        if (this.appointments != null) {
            this.appointments.forEach(i -> i.removeDesiredSubjects(this));
        }
        if (appointments != null) {
            appointments.forEach(i -> i.addDesiredSubjects(this));
        }
        this.appointments = appointments;
    }

    public Subject appointments(Set<Appointment> appointments) {
        this.setAppointments(appointments);
        return this;
    }

    public Subject addAppointments(Appointment appointment) {
        this.appointments.add(appointment);
        appointment.getDesiredSubjects().add(this);
        return this;
    }

    public Subject removeAppointments(Appointment appointment) {
        this.appointments.remove(appointment);
        appointment.getDesiredSubjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return getId() != null && getId().equals(((Subject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", credits=" + getCredits() +
            "}";
    }
}
