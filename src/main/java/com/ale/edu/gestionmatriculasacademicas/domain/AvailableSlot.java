package com.ale.edu.gestionmatriculasacademicas.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * A AvailableSlot.
 */
@Entity
@Table(name = "available_slot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AvailableSlot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    // endTime removed: slot duration is fixed to 30 minutes

    @NotNull
    @Column(name = "available_spots", nullable = false)
    private Integer availableSpots;

    @Column(name = "booked_spots")
    private Integer bookedSpots;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subjects" }, allowSetters = true)
    private Program program;

    @ManyToMany
    @JoinTable(
        name = "available_slot_advisors",
        joinColumns = @JoinColumn(name = "available_slot_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private java.util.Set<User> advisors = new java.util.HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AvailableSlot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public AvailableSlot startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    // endTime removed: duration fixed to 30 minutes

    public Integer getAvailableSpots() {
        return this.availableSpots;
    }

    public AvailableSlot availableSpots(Integer availableSpots) {
        this.setAvailableSpots(availableSpots);
        return this;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }

    public Integer getBookedSpots() {
        return this.bookedSpots;
    }

    public AvailableSlot bookedSpots(Integer bookedSpots) {
        this.setBookedSpots(bookedSpots);
        return this;
    }

    public void setBookedSpots(Integer bookedSpots) {
        this.bookedSpots = bookedSpots;
    }

    public Boolean getActive() {
        return this.active;
    }

    public AvailableSlot active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public AvailableSlot program(Program program) {
        this.setProgram(program);
        return this;
    }

    public java.util.Set<User> getAdvisors() { return this.advisors; }

    public void setAdvisors(java.util.Set<User> advisors) { this.advisors = advisors; }

    public AvailableSlot advisors(java.util.Set<User> advisors) { this.setAdvisors(advisors); return this; }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvailableSlot)) {
            return false;
        }
        return getId() != null && getId().equals(((AvailableSlot) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AvailableSlot{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", availableSpots=" + getAvailableSpots() +
            ", bookedSpots=" + getBookedSpots() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
