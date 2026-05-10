package com.ale.edu.gestionmatriculasacademicas.service.impl;

import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import com.ale.edu.gestionmatriculasacademicas.domain.Subject;
import com.ale.edu.gestionmatriculasacademicas.repository.ProgramRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.SubjectRepository;
import com.ale.edu.gestionmatriculasacademicas.service.ProgramService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.ProgramDTO;
import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.ProgramMapper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProgramServiceImpl implements ProgramService {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramServiceImpl.class);

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final SubjectRepository subjectRepository;

    public ProgramServiceImpl(
        ProgramRepository programRepository,
        ProgramMapper programMapper,
        SubjectRepository subjectRepository
    ) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public ProgramDTO save(ProgramDTO programDTO) {
        LOG.debug("Request to save Program : {}", programDTO);
        Program program = programMapper.toEntity(programDTO);
        program.setSubjects(resolveSubjects(programDTO));
        program = programRepository.save(program);
        return programMapper.toDto(program);
    }

    @Override
    public ProgramDTO update(ProgramDTO programDTO) {
        LOG.debug("Request to update Program : {}", programDTO);
        Program program = programMapper.toEntity(programDTO);
        program.setSubjects(resolveSubjects(programDTO));
        program = programRepository.save(program);
        return programMapper.toDto(program);
    }

    @Override
    public Optional<ProgramDTO> partialUpdate(ProgramDTO programDTO) {
        LOG.debug("Request to partially update Program : {}", programDTO);
        return programRepository.findById(programDTO.getId())
            .map(existingProgram -> {
                programMapper.partialUpdate(existingProgram, programDTO);
                if (programDTO.getSubjects() != null) {
                    existingProgram.setSubjects(resolveSubjects(programDTO));
                }
                return existingProgram;
            })
            .map(programRepository::save)
            .map(programMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgramDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Programs");
        return programRepository.findAll(pageable).map(programMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgramDTO> findOne(Long id) {
        LOG.debug("Request to get Program : {}", id);
        return programRepository.findOneWithEagerRelationships(id).map(programMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Program : {}", id);
        programRepository.deleteById(id);
    }

    // Resuelve los subjects desde los IDs que vienen en el DTO
    private Set<Subject> resolveSubjects(ProgramDTO programDTO) {
        Set<Subject> subjects = new HashSet<>();
        if (programDTO.getSubjects() != null) {
            for (SubjectDTO subjectDTO : programDTO.getSubjects()) {
                if (subjectDTO.getId() != null) {
                    subjectRepository.findById(subjectDTO.getId())
                        .ifPresent(subjects::add);
                }
            }
        }
        return subjects;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgramDTO> findAllWithEagerRelationships(Pageable pageable) {
        LOG.debug("Request to get all Programs with eager relationships");
        return programRepository.findAllWithEagerRelationships(pageable).map(programMapper::toDto);
    }
}