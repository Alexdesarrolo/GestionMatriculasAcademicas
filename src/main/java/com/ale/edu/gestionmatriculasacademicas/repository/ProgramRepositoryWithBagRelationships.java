package com.ale.edu.gestionmatriculasacademicas.repository;

import com.ale.edu.gestionmatriculasacademicas.domain.Program;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProgramRepositoryWithBagRelationships {
    Optional<Program> fetchBagRelationships(Optional<Program> program);

    List<Program> fetchBagRelationships(List<Program> programs);

    Page<Program> fetchBagRelationships(Page<Program> programs);
}
