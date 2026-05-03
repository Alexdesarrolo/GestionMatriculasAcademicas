package com.ale.edu.gestionmatriculasacademicas.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ale.edu.gestionmatriculasacademicas.domain.Enrollment} entity.
 */
public class EnrollmentDTO implements Serializable {

    private Long id;
    private Instant enrollmentDate;
    private StudentDTO student;
    private SubjectOfferingDTO subjectOffering;

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

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public SubjectOfferingDTO getSubjectOffering() {
        return subjectOffering;
    }

    public void setSubjectOffering(SubjectOfferingDTO subjectOffering) {
        this.subjectOffering = subjectOffering;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollmentDTO)) return false;
        return Objects.equals(id, ((EnrollmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
