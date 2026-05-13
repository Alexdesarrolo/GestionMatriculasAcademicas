package com.ale.edu.gestionmatriculasacademicas.service.impl;

import com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering;
import com.ale.edu.gestionmatriculasacademicas.repository.EnrollmentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.SubjectOfferingRepository;
import com.ale.edu.gestionmatriculasacademicas.service.SubjectOfferingService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectOfferingDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.SubjectOfferingMapper;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SubjectOffering}.
 */
@Service
@Transactional
public class SubjectOfferingServiceImpl implements SubjectOfferingService {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectOfferingServiceImpl.class);

    private final SubjectOfferingRepository subjectOfferingRepository;
    private final SubjectOfferingMapper subjectOfferingMapper;
    private final EnrollmentRepository enrollmentRepository;

    public SubjectOfferingServiceImpl(
        SubjectOfferingRepository subjectOfferingRepository,
        SubjectOfferingMapper subjectOfferingMapper,
        EnrollmentRepository enrollmentRepository
    ) {
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.subjectOfferingMapper = subjectOfferingMapper;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public SubjectOfferingDTO save(SubjectOfferingDTO subjectOfferingDTO) {
        LOG.debug("Request to save SubjectOffering : {}", subjectOfferingDTO);
        validateDuration(subjectOfferingDTO);
        SubjectOffering subjectOffering = subjectOfferingMapper.toEntity(subjectOfferingDTO);
        subjectOffering = subjectOfferingRepository.save(subjectOffering);
        return subjectOfferingMapper.toDto(subjectOffering);
    }

    @Override
    public SubjectOfferingDTO update(SubjectOfferingDTO subjectOfferingDTO) {
        LOG.debug("Request to update SubjectOffering : {}", subjectOfferingDTO);
        validateDuration(subjectOfferingDTO);
        SubjectOffering subjectOffering = subjectOfferingMapper.toEntity(subjectOfferingDTO);
        subjectOffering = subjectOfferingRepository.save(subjectOffering);
        return subjectOfferingMapper.toDto(subjectOffering);
    }

    private void validateDuration(SubjectOfferingDTO dto) {
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time must not be null");
        }
        Duration duration = Duration.between(dto.getStartTime(), dto.getEndTime());
        if (duration.isNegative()) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        long hours = duration.toHours();
        if (hours < 2 || hours > 8) {
            throw new IllegalArgumentException("Duration must be between 2 and 8 hours. Current: " + hours + " hours.");
        }
    }

    @Override
    public Optional<SubjectOfferingDTO> partialUpdate(SubjectOfferingDTO subjectOfferingDTO) {
        LOG.debug("Request to partially update SubjectOffering : {}", subjectOfferingDTO);
        return subjectOfferingRepository
            .findById(subjectOfferingDTO.getId())
            .map(existingSubjectOffering -> {
                subjectOfferingMapper.partialUpdate(existingSubjectOffering, subjectOfferingDTO);
                return existingSubjectOffering;
            })
            .map(subjectOfferingRepository::save)
            .map(subjectOfferingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubjectOfferingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SubjectOfferings with eager relationships");
        return subjectOfferingRepository.findAllWithEagerRelationships(pageable).map(this::enrichDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectOfferingDTO> findAll() {
        LOG.debug("Request to get all SubjectOfferings with eager relationships");
        return subjectOfferingRepository.findAllWithEagerRelationships().stream().map(this::enrichDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectOfferingDTO> findOne(Long id) {
        LOG.debug("Request to get SubjectOffering with eager relationships : {}", id);
        return subjectOfferingRepository.findOneWithEagerRelationships(id).map(this::enrichDto);
    }

    private SubjectOfferingDTO enrichDto(SubjectOffering subjectOffering) {
        SubjectOfferingDTO dto = subjectOfferingMapper.toDto(subjectOffering);
        dto.setEnrolledCount(enrollmentRepository.countBySubjectOfferingId(subjectOffering.getId()));
        return dto;
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SubjectOffering : {}", id);
        subjectOfferingRepository.deleteById(id);
    }
}
