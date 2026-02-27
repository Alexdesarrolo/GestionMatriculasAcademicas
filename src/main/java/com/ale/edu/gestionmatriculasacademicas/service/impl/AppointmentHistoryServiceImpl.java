package com.ale.edu.gestionmatriculasacademicas.service.impl;

import com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory;
import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentHistoryRepository;
import com.ale.edu.gestionmatriculasacademicas.service.AppointmentHistoryService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentHistoryDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.AppointmentHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory}.
 */
@Service
@Transactional
public class AppointmentHistoryServiceImpl implements AppointmentHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentHistoryServiceImpl.class);

    private final AppointmentHistoryRepository appointmentHistoryRepository;

    private final AppointmentHistoryMapper appointmentHistoryMapper;

    public AppointmentHistoryServiceImpl(
        AppointmentHistoryRepository appointmentHistoryRepository,
        AppointmentHistoryMapper appointmentHistoryMapper
    ) {
        this.appointmentHistoryRepository = appointmentHistoryRepository;
        this.appointmentHistoryMapper = appointmentHistoryMapper;
    }

    @Override
    public AppointmentHistoryDTO save(AppointmentHistoryDTO appointmentHistoryDTO) {
        LOG.debug("Request to save AppointmentHistory : {}", appointmentHistoryDTO);
        AppointmentHistory appointmentHistory = appointmentHistoryMapper.toEntity(appointmentHistoryDTO);
        appointmentHistory = appointmentHistoryRepository.save(appointmentHistory);
        return appointmentHistoryMapper.toDto(appointmentHistory);
    }

    @Override
    public AppointmentHistoryDTO update(AppointmentHistoryDTO appointmentHistoryDTO) {
        LOG.debug("Request to update AppointmentHistory : {}", appointmentHistoryDTO);
        AppointmentHistory appointmentHistory = appointmentHistoryMapper.toEntity(appointmentHistoryDTO);
        appointmentHistory = appointmentHistoryRepository.save(appointmentHistory);
        return appointmentHistoryMapper.toDto(appointmentHistory);
    }

    @Override
    public Optional<AppointmentHistoryDTO> partialUpdate(AppointmentHistoryDTO appointmentHistoryDTO) {
        LOG.debug("Request to partially update AppointmentHistory : {}", appointmentHistoryDTO);

        return appointmentHistoryRepository
            .findById(appointmentHistoryDTO.getId())
            .map(existingAppointmentHistory -> {
                appointmentHistoryMapper.partialUpdate(existingAppointmentHistory, appointmentHistoryDTO);

                return existingAppointmentHistory;
            })
            .map(appointmentHistoryRepository::save)
            .map(appointmentHistoryMapper::toDto);
    }

    public Page<AppointmentHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return appointmentHistoryRepository.findAllWithEagerRelationships(pageable).map(appointmentHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppointmentHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get AppointmentHistory : {}", id);
        return appointmentHistoryRepository.findOneWithEagerRelationships(id).map(appointmentHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AppointmentHistory : {}", id);
        appointmentHistoryRepository.deleteById(id);
    }
}
