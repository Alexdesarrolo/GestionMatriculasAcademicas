package com.ale.edu.gestionmatriculasacademicas.service;

import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentHistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory}.
 */
public interface AppointmentHistoryService {
    /**
     * Save a appointmentHistory.
     *
     * @param appointmentHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    AppointmentHistoryDTO save(AppointmentHistoryDTO appointmentHistoryDTO);

    /**
     * Updates a appointmentHistory.
     *
     * @param appointmentHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    AppointmentHistoryDTO update(AppointmentHistoryDTO appointmentHistoryDTO);

    /**
     * Partially updates a appointmentHistory.
     *
     * @param appointmentHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppointmentHistoryDTO> partialUpdate(AppointmentHistoryDTO appointmentHistoryDTO);

    /**
     * Get all the appointmentHistories with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppointmentHistoryDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" appointmentHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppointmentHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" appointmentHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<AppointmentHistoryDTO> findAll(Pageable pageable);

    Page<AppointmentHistoryDTO> findByAppointment(Long appointmentId, Pageable pageable);
}
