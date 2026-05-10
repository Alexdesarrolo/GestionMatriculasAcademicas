package com.ale.edu.gestionmatriculasacademicas.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvailableSlotDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime startTime;

    // endTime removed: slot duration is fixed to 30 minutes

    @NotNull
    private Integer availableSpots;

    private Integer bookedSpots;

    private Boolean active;

    private ProgramDTO program;
    private java.util.Set<com.ale.edu.gestionmatriculasacademicas.service.dto.UserDTO> advisors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    // endTime removed: use startTime and fixed duration

    public java.util.Set<com.ale.edu.gestionmatriculasacademicas.service.dto.UserDTO> getAdvisors() {
        return advisors;
    }

    public void setAdvisors(java.util.Set<com.ale.edu.gestionmatriculasacademicas.service.dto.UserDTO> advisors) {
        this.advisors = advisors;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }

    public Integer getBookedSpots() {
        return bookedSpots;
    }

    public void setBookedSpots(Integer bookedSpots) {
        this.bookedSpots = bookedSpots;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramDTO program) {
        this.program = program;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvailableSlotDTO)) {
            return false;
        }

        AvailableSlotDTO availableSlotDTO = (AvailableSlotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, availableSlotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvailableSlotDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime removed" +
            ", availableSpots=" + getAvailableSpots() +
            ", bookedSpots=" + getBookedSpots() +
            ", active='" + getActive() + "'" +
            ", program=" + getProgram() +
            "}";
    }
}
