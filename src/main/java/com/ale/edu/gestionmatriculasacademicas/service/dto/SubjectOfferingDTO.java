package com.ale.edu.gestionmatriculasacademicas.service.dto;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.DayOfWeek;

/**
 * A DTO for the {@link com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering} entity.
 */
public class SubjectOfferingDTO implements Serializable {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private String semester;
    private Integer capacity;
    private SubjectDTO subject;
    private ProfessorDTO professor;
    private Long enrolledCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public ProfessorDTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDTO professor) {
        this.professor = professor;
    }

    public Long getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(Long enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectOfferingDTO)) return false;
        return Objects.equals(id, ((SubjectOfferingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
