package com.ale.edu.gestionmatriculasacademicas.service;

import com.ale.edu.gestionmatriculasacademicas.service.dto.SubjectOfferingDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.SubjectOffering}.
 */
public interface SubjectOfferingService {
    SubjectOfferingDTO save(SubjectOfferingDTO subjectOfferingDTO);
    SubjectOfferingDTO update(SubjectOfferingDTO subjectOfferingDTO);
    Optional<SubjectOfferingDTO> partialUpdate(SubjectOfferingDTO subjectOfferingDTO);
    Page<SubjectOfferingDTO> findAll(Pageable pageable);
    Optional<SubjectOfferingDTO> findOne(Long id);
    void delete(Long id);
    List<SubjectOfferingDTO> findAll();
}
