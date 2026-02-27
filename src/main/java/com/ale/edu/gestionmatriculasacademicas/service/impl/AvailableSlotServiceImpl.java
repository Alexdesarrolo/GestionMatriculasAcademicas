package com.ale.edu.gestionmatriculasacademicas.service.impl;

import com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot;
import com.ale.edu.gestionmatriculasacademicas.repository.AvailableSlotRepository;
import com.ale.edu.gestionmatriculasacademicas.service.AvailableSlotService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.AvailableSlotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot}.
 */
@Service
@Transactional
public class AvailableSlotServiceImpl implements AvailableSlotService {

    private static final Logger LOG = LoggerFactory.getLogger(AvailableSlotServiceImpl.class);

    private final AvailableSlotRepository availableSlotRepository;

    private final AvailableSlotMapper availableSlotMapper;

    public AvailableSlotServiceImpl(AvailableSlotRepository availableSlotRepository, AvailableSlotMapper availableSlotMapper) {
        this.availableSlotRepository = availableSlotRepository;
        this.availableSlotMapper = availableSlotMapper;
    }

    @Override
    public AvailableSlotDTO save(AvailableSlotDTO availableSlotDTO) {
        LOG.debug("Request to save AvailableSlot : {}", availableSlotDTO);
        AvailableSlot availableSlot = availableSlotMapper.toEntity(availableSlotDTO);
        availableSlot = availableSlotRepository.save(availableSlot);
        return availableSlotMapper.toDto(availableSlot);
    }

    @Override
    public AvailableSlotDTO update(AvailableSlotDTO availableSlotDTO) {
        LOG.debug("Request to update AvailableSlot : {}", availableSlotDTO);
        AvailableSlot availableSlot = availableSlotMapper.toEntity(availableSlotDTO);
        availableSlot = availableSlotRepository.save(availableSlot);
        return availableSlotMapper.toDto(availableSlot);
    }

    @Override
    public Optional<AvailableSlotDTO> partialUpdate(AvailableSlotDTO availableSlotDTO) {
        LOG.debug("Request to partially update AvailableSlot : {}", availableSlotDTO);

        return availableSlotRepository
            .findById(availableSlotDTO.getId())
            .map(existingAvailableSlot -> {
                availableSlotMapper.partialUpdate(existingAvailableSlot, availableSlotDTO);

                return existingAvailableSlot;
            })
            .map(availableSlotRepository::save)
            .map(availableSlotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AvailableSlotDTO> findOne(Long id) {
        LOG.debug("Request to get AvailableSlot : {}", id);
        return availableSlotRepository.findById(id).map(availableSlotMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AvailableSlot : {}", id);
        availableSlotRepository.deleteById(id);
    }
}
