package com.ale.edu.gestionmatriculasacademicas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;
import com.ale.edu.gestionmatriculasacademicas.domain.enumeration.AppointmentStatus;

/**
 * Spring Data JPA repository for the Appointment entity.
 *
 * When extending this class, extend AppointmentRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AppointmentRepository
    extends AppointmentRepositoryWithBagRelationships, JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    default Optional<Appointment> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    

    default Page<Appointment> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    Page<Appointment> findByStudentId(Long studentId, Pageable pageable);

    List<Appointment> findByStudentIdAndStatusIn(Long studentId, List<AppointmentStatus> statuses);

    @Query(value = "SELECT a FROM Appointment a LEFT JOIN FETCH a.student LEFT JOIN FETCH a.availableSlot",
       countQuery = "SELECT COUNT(a) FROM Appointment a")
List<Appointment> findAllWithRelationships();

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.availableSlot.id = :slotId AND a.advisor.id = :advisorId")
    long countByAvailableSlotIdAndAdvisorId(@Param("slotId") Long slotId, @Param("advisorId") Long advisorId);

    Page<Appointment> findByAdvisorId(Long advisorId, Pageable pageable);
}
