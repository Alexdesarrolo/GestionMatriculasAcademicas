// package com.ale.edu.gestionmatriculasacademicas.web.rest;

// import com.ale.edu.gestionmatriculasacademicas.repository.AppointmentHistoryRepository;
// import com.ale.edu.gestionmatriculasacademicas.service.AppointmentHistoryQueryService;
// import com.ale.edu.gestionmatriculasacademicas.service.AppointmentHistoryService;
// import com.ale.edu.gestionmatriculasacademicas.service.criteria.AppointmentHistoryCriteria;
// import com.ale.edu.gestionmatriculasacademicas.service.dto.AppointmentHistoryDTO;
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
//  * REST controller for managing {@link com.ale.edu.gestionmatriculasacademicas.domain.AppointmentHistory}.
//  */
// @RestController
// @RequestMapping("/api/appointment-histories")
// public class AppointmentHistoryResource {

//     private static final Logger LOG = LoggerFactory.getLogger(AppointmentHistoryResource.class);

//     private static final String ENTITY_NAME = "appointmentHistory";

//     @Value("${jhipster.clientApp.name}")
//     private String applicationName;

//     private final AppointmentHistoryService appointmentHistoryService;

//     private final AppointmentHistoryRepository appointmentHistoryRepository;

//     private final AppointmentHistoryQueryService appointmentHistoryQueryService;

//     public AppointmentHistoryResource(
//         AppointmentHistoryService appointmentHistoryService,
//         AppointmentHistoryRepository appointmentHistoryRepository,
//         AppointmentHistoryQueryService appointmentHistoryQueryService
//     ) {
//         this.appointmentHistoryService = appointmentHistoryService;
//         this.appointmentHistoryRepository = appointmentHistoryRepository;
//         this.appointmentHistoryQueryService = appointmentHistoryQueryService;
//     }

//     /**
//      * {@code POST  /appointment-histories} : Create a new appointmentHistory.
//      *
//      * @param appointmentHistoryDTO the appointmentHistoryDTO to create.
//      * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentHistoryDTO, or with status {@code 400 (Bad Request)} if the appointmentHistory has already an ID.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PostMapping("")
//     public ResponseEntity<AppointmentHistoryDTO> createAppointmentHistory(@Valid @RequestBody AppointmentHistoryDTO appointmentHistoryDTO)
//         throws URISyntaxException {
//         LOG.debug("REST request to save AppointmentHistory : {}", appointmentHistoryDTO);
//         if (appointmentHistoryDTO.getId() != null) {
//             throw new BadRequestAlertException("A new appointmentHistory cannot already have an ID", ENTITY_NAME, "idexists");
//         }
//         appointmentHistoryDTO = appointmentHistoryService.save(appointmentHistoryDTO);
//         return ResponseEntity.created(new URI("/api/appointment-histories/" + appointmentHistoryDTO.getId()))
//             .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appointmentHistoryDTO.getId().toString()))
//             .body(appointmentHistoryDTO);
//     }

//     /**
//      * {@code PUT  /appointment-histories/:id} : Updates an existing appointmentHistory.
//      *
//      * @param id the id of the appointmentHistoryDTO to save.
//      * @param appointmentHistoryDTO the appointmentHistoryDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentHistoryDTO,
//      * or with status {@code 400 (Bad Request)} if the appointmentHistoryDTO is not valid,
//      * or with status {@code 500 (Internal Server Error)} if the appointmentHistoryDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PutMapping("/{id}")
//     public ResponseEntity<AppointmentHistoryDTO> updateAppointmentHistory(
//         @PathVariable(value = "id", required = false) final Long id,
//         @Valid @RequestBody AppointmentHistoryDTO appointmentHistoryDTO
//     ) throws URISyntaxException {
//         LOG.debug("REST request to update AppointmentHistory : {}, {}", id, appointmentHistoryDTO);
//         if (appointmentHistoryDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         if (!Objects.equals(id, appointmentHistoryDTO.getId())) {
//             throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//         }

//         if (!appointmentHistoryRepository.existsById(id)) {
//             throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//         }

//         appointmentHistoryDTO = appointmentHistoryService.update(appointmentHistoryDTO);
//         return ResponseEntity.ok()
//             .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentHistoryDTO.getId().toString()))
//             .body(appointmentHistoryDTO);
//     }

//     /**
//      * {@code PATCH  /appointment-histories/:id} : Partial updates given fields of an existing appointmentHistory, field will ignore if it is null
//      *
//      * @param id the id of the appointmentHistoryDTO to save.
//      * @param appointmentHistoryDTO the appointmentHistoryDTO to update.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentHistoryDTO,
//      * or with status {@code 400 (Bad Request)} if the appointmentHistoryDTO is not valid,
//      * or with status {@code 404 (Not Found)} if the appointmentHistoryDTO is not found,
//      * or with status {@code 500 (Internal Server Error)} if the appointmentHistoryDTO couldn't be updated.
//      * @throws URISyntaxException if the Location URI syntax is incorrect.
//      */
//     @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
//     public ResponseEntity<AppointmentHistoryDTO> partialUpdateAppointmentHistory(
//         @PathVariable(value = "id", required = false) final Long id,
//         @NotNull @RequestBody AppointmentHistoryDTO appointmentHistoryDTO
//     ) throws URISyntaxException {
//         LOG.debug("REST request to partial update AppointmentHistory partially : {}, {}", id, appointmentHistoryDTO);
//         if (appointmentHistoryDTO.getId() == null) {
//             throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//         }
//         if (!Objects.equals(id, appointmentHistoryDTO.getId())) {
//             throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//         }

//         if (!appointmentHistoryRepository.existsById(id)) {
//             throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//         }

//         Optional<AppointmentHistoryDTO> result = appointmentHistoryService.partialUpdate(appointmentHistoryDTO);

//         return ResponseUtil.wrapOrNotFound(
//             result,
//             HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appointmentHistoryDTO.getId().toString())
//         );
//     }

//     /**
//      * {@code GET  /appointment-histories} : get all the appointmentHistories.
//      *
//      * @param pageable the pagination information.
//      * @param criteria the criteria which the requested entities should match.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointmentHistories in body.
//      */
//     @GetMapping("")
//     public ResponseEntity<List<AppointmentHistoryDTO>> getAllAppointmentHistories(
//         AppointmentHistoryCriteria criteria,
//         @org.springdoc.core.annotations.ParameterObject Pageable pageable
//     ) {
//         LOG.debug("REST request to get AppointmentHistories by criteria: {}", criteria);

//         Page<AppointmentHistoryDTO> page = appointmentHistoryQueryService.findByCriteria(criteria, pageable);
//         HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//         return ResponseEntity.ok().headers(headers).body(page.getContent());
//     }

//     /**
//      * {@code GET  /appointment-histories/count} : count all the appointmentHistories.
//      *
//      * @param criteria the criteria which the requested entities should match.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
//      */
//     @GetMapping("/count")
//     public ResponseEntity<Long> countAppointmentHistories(AppointmentHistoryCriteria criteria) {
//         LOG.debug("REST request to count AppointmentHistories by criteria: {}", criteria);
//         return ResponseEntity.ok().body(appointmentHistoryQueryService.countByCriteria(criteria));
//     }

//     /**
//      * {@code GET  /appointment-histories/:id} : get the "id" appointmentHistory.
//      *
//      * @param id the id of the appointmentHistoryDTO to retrieve.
//      * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentHistoryDTO, or with status {@code 404 (Not Found)}.
//      */
//     @GetMapping("/{id}")
//     public ResponseEntity<AppointmentHistoryDTO> getAppointmentHistory(@PathVariable("id") Long id) {
//         LOG.debug("REST request to get AppointmentHistory : {}", id);
//         Optional<AppointmentHistoryDTO> appointmentHistoryDTO = appointmentHistoryService.findOne(id);
//         return ResponseUtil.wrapOrNotFound(appointmentHistoryDTO);
//     }

//     /**
//      * {@code DELETE  /appointment-histories/:id} : delete the "id" appointmentHistory.
//      *
//      * @param id the id of the appointmentHistoryDTO to delete.
//      * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//      */
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteAppointmentHistory(@PathVariable("id") Long id) {
//         LOG.debug("REST request to delete AppointmentHistory : {}", id);
//         appointmentHistoryService.delete(id);
//         return ResponseEntity.noContent()
//             .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//             .build();
//     }
// }
