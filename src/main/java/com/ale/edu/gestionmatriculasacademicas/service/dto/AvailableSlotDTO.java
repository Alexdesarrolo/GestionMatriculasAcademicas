package com.ale.edu.gestionmatriculasacademicas.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvailableSlotDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime startTime;

    @NotNull
    private ZonedDateTime endTime;

    @NotNull
    private Integer availableSpots;

    private Integer bookedSpots;

    private Boolean active;

    private ProgramDTO program;

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

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
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
            ", endTime='" + getEndTime() + "'" +
            ", availableSpots=" + getAvailableSpots() +
            ", bookedSpots=" + getBookedSpots() +
            ", active='" + getActive() + "'" +
            ", program=" + getProgram() +
            "}";
    }
}
