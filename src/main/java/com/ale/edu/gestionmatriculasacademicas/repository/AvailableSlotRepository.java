package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AvailableSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long>, JpaSpecificationExecutor<AvailableSlot> {}
