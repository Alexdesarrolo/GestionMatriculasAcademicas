package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    default Optional<Student> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Student> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Student> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select student from Student student left join fetch student.user",
        countQuery = "select count(student) from Student student"
    )
    Page<Student> findAllWithToOneRelationships(Pageable pageable);

    @Query("select student from Student student left join fetch student.user")
    List<Student> findAllWithToOneRelationships();

    @Query("select student from Student student left join fetch student.user where student.id =:id")
    Optional<Student> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select student from Student student join fetch student.user where student.user.login = :login")
    Optional<Student> findByUserLogin(@Param("login") String login);

    boolean existsByStudentCode(String studentCode);

    boolean existsByNationalId(String nationalId);

    long countByStudentCodeStartingWith(String prefix);
}
