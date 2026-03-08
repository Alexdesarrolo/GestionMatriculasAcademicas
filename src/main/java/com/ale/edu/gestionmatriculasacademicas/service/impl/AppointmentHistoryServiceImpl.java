package com.ale.edu.gestionmatriculasacademicas.service.impl;

import com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory;
import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentHistoryRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.UserRepository;
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

@Service
@Transactional
public class AppointmentHistoryServiceImpl implements AppointmentHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentHistoryServiceImpl.class);

    private final AppointmentHistoryRepository appointmentHistoryRepository;
    private final AppointmentHistoryMapper appointmentHistoryMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentHistoryServiceImpl(
        AppointmentHistoryRepository appointmentHistoryRepository,
        AppointmentHistoryMapper appointmentHistoryMapper,
        AppointmentRepository appointmentRepository,
        UserRepository userRepository
    ) {
        this.appointmentHistoryRepository = appointmentHistoryRepository;
        this.appointmentHistoryMapper = appointmentHistoryMapper;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AppointmentHistoryDTO save(AppointmentHistoryDTO appointmentHistoryDTO) {
        LOG.debug("Request to save AppointmentHistory : {}", appointmentHistoryDTO);
        AppointmentHistory appointmentHistory = appointmentHistoryMapper.toEntity(appointmentHistoryDTO);
        resolveRelations(appointmentHistory, appointmentHistoryDTO);
        appointmentHistory = appointmentHistoryRepository.save(appointmentHistory);
        return appointmentHistoryMapper.toDto(appointmentHistory);
    }

    @Override
    public AppointmentHistoryDTO update(AppointmentHistoryDTO appointmentHistoryDTO) {
        LOG.debug("Request to update AppointmentHistory : {}", appointmentHistoryDTO);
        AppointmentHistory appointmentHistory = appointmentHistoryMapper.toEntity(appointmentHistoryDTO);
        resolveRelations(appointmentHistory, appointmentHistoryDTO);
        appointmentHistory = appointmentHistoryRepository.save(appointmentHistory);
        return appointmentHistoryMapper.toDto(appointmentHistory);
    }

    @Override
    public Optional<AppointmentHistoryDTO> partialUpdate(AppointmentHistoryDTO appointmentHistoryDTO) {
        LOG.debug("Request to partially update AppointmentHistory : {}", appointmentHistoryDTO);
        return appointmentHistoryRepository.findById(appointmentHistoryDTO.getId())
            .map(existingHistory -> {
                appointmentHistoryMapper.partialUpdate(existingHistory, appointmentHistoryDTO);
                resolveRelations(existingHistory, appointmentHistoryDTO);
                return existingHistory;
            })
            .map(appointmentHistoryRepository::save)
            .map(appointmentHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentHistoryDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AppointmentHistories");
        return appointmentHistoryRepository.findAll(pageable).map(appointmentHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentHistoryDTO> findAllWithEagerRelationships(Pageable pageable) {
        LOG.debug("Request to get all AppointmentHistories with eager relationships");
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

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentHistoryDTO> findByAppointment(Long appointmentId, Pageable pageable) {
        LOG.debug("Request to get AppointmentHistory by appointment : {}", appointmentId);
        return appointmentHistoryRepository.findByAppointmentId(appointmentId, pageable)
            .map(appointmentHistoryMapper::toDto);
    }

    private void resolveRelations(AppointmentHistory history, AppointmentHistoryDTO dto) {
        if (dto.getAppointment() != null && dto.getAppointment().getId() != null) {
            appointmentRepository.findById(dto.getAppointment().getId())
                .ifPresent(history::setAppointment);
        }
        if (dto.getPerformedBy() != null && dto.getPerformedBy().getId() != null) {
            userRepository.findById(dto.getPerformedBy().getId())
                .ifPresent(history::setPerformedBy);
        }
    }
}