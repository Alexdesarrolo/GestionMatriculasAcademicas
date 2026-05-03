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
 * A Professor.
 */
@Entity
@Table(name = "professor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Professor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "national_id", nullable = false, unique = true)
    private String nationalId;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "professor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "professor", "subject" }, allowSetters = true)
    private Set<SubjectOffering> subjectOfferings = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Professor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Professor firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Professor lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Professor email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalId() {
        return this.nationalId;
    }

    public Professor nationalId(String nationalId) {
        this.setNationalId(nationalId);
        return this;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Professor active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<SubjectOffering> getSubjectOfferings() {
        return this.subjectOfferings;
    }

    public void setSubjectOfferings(Set<SubjectOffering> subjectOfferings) {
        if (this.subjectOfferings != null) {
            this.subjectOfferings.forEach(i -> i.setProfessor(null));
        }
        if (subjectOfferings != null) {
            subjectOfferings.forEach(i -> i.setProfessor(this));
        }
        this.subjectOfferings = subjectOfferings;
    }

    public Professor subjectOfferings(Set<SubjectOffering> subjectOfferings) {
        this.setSubjectOfferings(subjectOfferings);
        return this;
    }

    public Professor addSubjectOffering(SubjectOffering subjectOffering) {
        this.subjectOfferings.add(subjectOffering);
        subjectOffering.setProfessor(this);
        return this;
    }

    public Professor removeSubjectOffering(SubjectOffering subjectOffering) {
        this.subjectOfferings.remove(subjectOffering);
        subjectOffering.setProfessor(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professor)) {
            return false;
        }
        return getId() != null && getId().equals(((Professor) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Professor{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", nationalId='" + getNationalId() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
