package com.ale.edu.gestionmatriculasacademicas.service;

import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot}.
 */
public interface AvailableSlotService {
    /**
     * Save a availableSlot.
     *
     * @param availableSlotDTO the entity to save.
     * @return the persisted entity.
     */
    AvailableSlotDTO save(AvailableSlotDTO availableSlotDTO);

    /**
     * Updates a availableSlot.
     *
     * @param availableSlotDTO the entity to update.
     * @return the persisted entity.
     */
    AvailableSlotDTO update(AvailableSlotDTO availableSlotDTO);

    /**
     * Partially updates a availableSlot.
     *
     * @param availableSlotDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AvailableSlotDTO> partialUpdate(AvailableSlotDTO availableSlotDTO);

    /**
     * Get the "id" availableSlot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AvailableSlotDTO> findOne(Long id);

    /**
     * Delete the "id" availableSlot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<AvailableSlotDTO> findAll(Pageable pageable);
}
