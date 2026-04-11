package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.Appointment;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

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

    @Query(value = "SELECT a FROM Appointment a LEFT JOIN FETCH a.student LEFT JOIN FETCH a.availableSlot",
       countQuery = "SELECT COUNT(a) FROM Appointment a")
List<Appointment> findAllWithRelationships();



}
