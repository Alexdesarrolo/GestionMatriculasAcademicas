package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppointmentHistory entity.
 */
@Repository
public interface AppointmentHistoryRepository
    extends JpaRepository<AppointmentHistory, Long>, JpaSpecificationExecutor<AppointmentHistory> {
    @Query(
        "select appointmentHistory from AppointmentHistory appointmentHistory where appointmentHistory.performedBy.login = ?#{authentication.name}"
    )
    List<AppointmentHistory> findByPerformedByIsCurrentUser();

    default Optional<AppointmentHistory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AppointmentHistory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AppointmentHistory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select appointmentHistory from AppointmentHistory appointmentHistory left join fetch appointmentHistory.performedBy",
        countQuery = "select count(appointmentHistory) from AppointmentHistory appointmentHistory"
    )
    Page<AppointmentHistory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select appointmentHistory from AppointmentHistory appointmentHistory left join fetch appointmentHistory.performedBy")
    List<AppointmentHistory> findAllWithToOneRelationships();

    @Query(
        "select appointmentHistory from AppointmentHistory appointmentHistory left join fetch appointmentHistory.performedBy where appointmentHistory.id =:id"
    )
    Optional<AppointmentHistory> findOneWithToOneRelationships(@Param("id") Long id);
}
