package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubjectOffering entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectOfferingRepository extends JpaRepository<SubjectOffering, Long> {}
