package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the AvailableSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long>, JpaSpecificationExecutor<AvailableSlot> {

    @EntityGraph(attributePaths = { "program", "advisors" })
    Page<AvailableSlot> findAll(Pageable pageable);

    @EntityGraph(attributePaths = { "program", "advisors" })
    Optional<AvailableSlot> findById(Long id);

    @EntityGraph(attributePaths = { "program", "advisors" })
    List<AvailableSlot> findAll();
}
