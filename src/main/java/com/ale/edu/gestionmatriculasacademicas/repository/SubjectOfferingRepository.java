package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubjectOffering entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectOfferingRepository extends JpaRepository<SubjectOffering, Long> {

    @Query(value = "select offering from SubjectOffering offering " +
                   "left join fetch offering.subject " +
                   "left join fetch offering.professor",
           countQuery = "select count(offering) from SubjectOffering offering")
    Page<SubjectOffering> findAllWithEagerRelationships(Pageable pageable);

    @Query("select offering from SubjectOffering offering " +
           "left join fetch offering.subject " +
           "left join fetch offering.professor")
    List<SubjectOffering> findAllWithEagerRelationships();

    @Query("select offering from SubjectOffering offering " +
           "left join fetch offering.subject " +
           "left join fetch offering.professor " +
           "where offering.id = :id")
    Optional<SubjectOffering> findOneWithEagerRelationships(@Param("id") Long id);
}
