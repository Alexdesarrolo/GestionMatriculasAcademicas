package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.Enrollment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Enrollment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    long countBySubjectOfferingId(Long subjectOfferingId);
}
