// package com.ale.edu.gestionmatriculasacademicas.web.rest;

// import com.ale.edu.gestionmatriculasacademicas.repository.AvailableSlotRepository;
// import com.ale.edu.gestionmatriculasacademicas.service.AvailableSlotQueryService;
// import com.ale.edu.gestionmatriculasacademicas.service.AvailableSlotService;
// import com.ale.edu.gestionmatriculasacademicas.service.criteria.AvailableSlotCriteria;
// import com.ale.edu.gestionmatriculasacademicas.service.dto.AvailableSlotDTO;
// import com.ale.edu.gestionmatriculasacademicas.web.rest.errors.BadRequestAlertException;
// import jakarta.validation.Valid;
// import jakarta.validation.constraints.NotNull;
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.util.List;
// import java.util.Objects;
// import java.util.Optional;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
// import tech.jhipster.web.util.HeaderUtil;
// import tech.jhipster.web.util.PaginationUtil;
// import tech.jhipster.web.util.ResponseUtil;

// /**
//  * REST controller for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.AvailableSlot}.
//  */
// @RestController
// @RequestMapping("/api/available-slots")
// public class AvailableSlotResource {

//     private static final Logger LOG = LoggerFactory.getLogger(AvailableSlotResource.class);

//     private static final String ENTITY_NAME = "availableSlot";

//     @Value("${jhipster.clientApp.name}")
//     private String applicationName;

//     private final AvailableSlotService availableSlotService;

//     private final AvailableSlotRepository availableSlotRepository;

//     private final AvailableSlotQueryService availableSlotQueryService;

//     public AvailableSlotResource(
//         AvailableSlotService availableSlotService,
//         AvailableSlotRepository availableSlotRepository,
//         AvailableSlotQueryService availableSlotQueryService
//     ) {
//         this.availableSlotService = availableSlotService;
//         this.availableSlotRepository = availableSlotRepository;
//         this.availableSlotQueryService = availableSlotQueryService;
//     }

//     /**
//      * {@code POST  /available-slots} : Create a new availableSlot.
//      *
//      * @param availableSlotDTO the availableSlotDTO to create.
//      * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new availableSlotDTO, or with status {@code 400 (Bad Request)} if the availableSlot has already an ID.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PostMapping("")
//     public ResponseEntity<AvailableSlotDTO> createAvailableSlot(@Valid @RequestBody AvailableSlotDTO availableSlotDTO)
//         throws URISyntaxException {
//         LOG.debug("REST request to save AvailableSlot : {}", availableSlotDTO);
//         if (availableSlotDTO.getId() != null) {
//             throw new BadRequestAlertException("A new availableSlot cannot already have an ID", ENTITY_NAME, "idexists");
//         }
//         availableSlotDTO = availableSlotService.save(availableSlotDTO);
//         return ResponseEntity.created(new URI("/api/available-slots/" + availableSlotDTO.getId()))
//             .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, availableSlotDTO.getId().toString()))
//             .body(availableSlotDTO);
//     }

//     /**
//      * {@code PUT  /available-slots/:id} : Updates an existing availableSlot.
//      *
//      * @param id the id of the availableSlotDTO to save.
//      * @param availableSlotDTO the availableSlotDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated availableSlotDTO,
//      * or with status {@code 400 (Bad Request)} if the availableSlotDTO is not valid,
//      * or with status {@code 500 (Internal Server Error)} if the availableSlotDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PutMapping("/{id}")
//     public ResponseEntity<AvailableSlotDTO> updateAvailableSlot(
//         @PathVariable(value = "id", required = false) final Long id,
//         @Valid @RequestBody AvailableSlotDTO availableSlotDTO
//     ) throws URISyntaxException {
//         LOG.debug("REST request to update AvailableSlot : {}, {}", id, availableSlotDTO);
//         if (availableSlotDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         if (!Objects.equals(id, availableSlotDTO.getId())) {
//             throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//         }

//         if (!availableSlotRepository.existsById(id)) {
//             throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//         }

//         availableSlotDTO = availableSlotService.update(availableSlotDTO);
//         return ResponseEntity.ok()
//             .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, availableSlotDTO.getId().toString()))
//             .body(availableSlotDTO);
//     }

//     /**
//      * {@code PATCH  /available-slots/:id} : Partial updates given fields of an existing availableSlot, field will ignore if it is null
//      *
//      * @param id the id of the availableSlotDTO to save.
//      * @param availableSlotDTO the availableSlotDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated availableSlotDTO,
//      * or with status {@code 400 (Bad Request)} if the availableSlotDTO is not valid,
//      * or with status {@code 404 (Not Found)} if the availableSlotDTO is not found,
//      * or with status {@code 500 (Internal Server Error)} if the availableSlotDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
//     public ResponseEntity<AvailableSlotDTO> partialUpdateAvailableSlot(
//         @PathVariable(value = "id", required = false) final Long id,
//         @NotNull @RequestBody AvailableSlotDTO availableSlotDTO
//     ) throws URISyntaxException {
//         LOG.debug("REST request to partial update AvailableSlot partially : {}, {}", id, availableSlotDTO);
//         if (availableSlotDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         if (!Objects.equals(id, availableSlotDTO.getId())) {
//             throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//         }

//         if (!availableSlotRepository.existsById(id)) {
//             throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//         }

//         Optional<AvailableSlotDTO> result = availableSlotService.partialUpdate(availableSlotDTO);

//         return ResponseUtil.wrapOrNotFound(
//             result,
//             HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, availableSlotDTO.getId().toString())
//         );
//     }

//     /**
//      * {@code GET  /available-slots} : get all the availableSlots.
//      *
//      * @param pageable the pagination information.
//      * @param criteria the criteria which the requested entities should match.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of availableSlots in body.
//      */
//     @GetMapping("")
//     public ResponseEntity<List<AvailableSlotDTO>> getAllAvailableSlots(
//         AvailableSlotCriteria criteria,
//         @org.springdoc.core.annotations.ParameterObject Pageable pageable
//     ) {
//         LOG.debug("REST request to get AvailableSlots by criteria: {}", criteria);

//         Page<AvailableSlotDTO> page = availableSlotQueryService.findByCriteria(criteria, pageable);
//         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//         return ResponseEntity.ok().headers(headers).body(page.getContent());
//     }

//     /**
//      * {@code GET  /available-slots/count} : count all the availableSlots.
//      *
//      * @param criteria the criteria which the requested entities should match.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
//      */
//     @GetMapping("/count")
//     public ResponseEntity<Long> countAvailableSlots(AvailableSlotCriteria criteria) {
//         LOG.debug("REST request to count AvailableSlots by criteria: {}", criteria);
//         return ResponseEntity.ok().body(availableSlotQueryService.countByCriteria(criteria));
//     }

//     /**
//      * {@code GET  /available-slots/:id} : get the "id" availableSlot.
//      *
//      * @param id the id of the availableSlotDTO to retrieve.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the availableSlotDTO, or with status {@code 404 (Not Found)}.
//      */
//     @GetMapping("/{id}")
//     public ResponseEntity<AvailableSlotDTO> getAvailableSlot(@PathVariable("id") Long id) {
//         LOG.debug("REST request to get AvailableSlot : {}", id);
//         Optional<AvailableSlotDTO> availableSlotDTO = availableSlotService.findOne(id);
//         return ResponseUtil.wrapOrNotFound(availableSlotDTO);
//     }

//     /**
//      * {@code DELETE  /available-slots/:id} : delete the "id" availableSlot.
//      *
//      * @param id the id of the availableSlotDTO to delete.
//      * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//      */
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteAvailableSlot(@PathVariable("id") Long id) {
//         LOG.debug("REST request to delete AvailableSlot : {}", id);
//         availableSlotService.delete(id);
//         return ResponseEntity.noContent()
//             .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//             .build();
//     }
// }
