package com.ale.edu.gestionmatriculasacademicas.service.impl;

import com.ale.edu.gestionmatriculasacademicas.domain.Student;
import com.ale.edu.gestionmatriculasacademicas.repository.ProgramRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.StudentRepository;
import com.ale.edu.gestionmatriculasacademicas.repository.UserRepository;
import com.ale.edu.gestionmatriculasacademicas.service.StudentService;
import com.ale.edu.gestionmatriculasacademicas.service.dto.StudentDTO;
import com.ale.edu.gestionmatriculasacademicas.service.mapper.StudentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final ProgramRepository programRepository;

    public StudentServiceImpl(
        StudentRepository studentRepository,
        StudentMapper studentMapper,
        UserRepository userRepository,
        ProgramRepository programRepository
    ) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userRepository = userRepository;
        this.programRepository = programRepository;
    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        LOG.debug("Request to save Student : {}", studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        resolveRelations(student, studentDTO);
        student = studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    @Override
    public StudentDTO update(StudentDTO studentDTO) {
        LOG.debug("Request to update Student : {}", studentDTO);
        Student student = studentMapper.toEntity(studentDTO);
        resolveRelations(student, studentDTO);
        student = studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    @Override
    public Optional<StudentDTO> partialUpdate(StudentDTO studentDTO) {
        LOG.debug("Request to partially update Student : {}", studentDTO);
        return studentRepository.findById(studentDTO.getId())
            .map(existingStudent -> {
                studentMapper.partialUpdate(existingStudent, studentDTO);
                resolveRelations(existingStudent, studentDTO);
                return existingStudent;
            })
            .map(studentRepository::save)
            .map(studentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Students");
        return studentRepository.findAll(pageable).map(studentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentDTO> findOne(Long id) {
        LOG.debug("Request to get Student : {}", id);
        return studentRepository.findOneWithEagerRelationships(id).map(studentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }

    // Resuelve user y program desde los IDs del DTO
    private void resolveRelations(Student student, StudentDTO studentDTO) {
        if (studentDTO.getUser() != null && studentDTO.getUser().getId() != null) {
            userRepository.findById(studentDTO.getUser().getId())
                .ifPresent(student::setUser);
        }
        if (studentDTO.getProgram() != null && studentDTO.getProgram().getId() != null) {
            programRepository.findById(studentDTO.getProgram().getId())
                .ifPresent(student::setProgram);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentDTO> findAllWithEagerRelationships(Pageable pageable) {
        LOG.debug("Request to get all Students with eager relationships");
        return studentRepository.findAllWithEagerRelationships(pageable).map(studentMapper::toDto);
    }

    @Override
    public Optional<StudentDTO> findOneByUserLogin(String login) {
        LOG.debug("Request to get Student by login : {}", login);
      return studentRepository.findByUserLogin(login).map(studentMapper::toDto);
    }
}