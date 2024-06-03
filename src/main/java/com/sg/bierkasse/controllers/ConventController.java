package com.sg.bierkasse.controllers;

import com.sg.bierkasse.dtos.ConventDTO;
import com.sg.bierkasse.services.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/convente")
public class ConventController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConventController.class);
    private final EntityService<ConventDTO> conventService;

    public ConventController(EntityService<ConventDTO> conventService) {
        this.conventService = conventService;
    }

    @PostMapping("person")
    @ResponseStatus(HttpStatus.CREATED)
    public ConventDTO postPerson(@RequestBody ConventDTO conventDTO) {
        return conventService.save(conventDTO);
    }

    @PostMapping("persons")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ConventDTO> postPersons(@RequestBody List<ConventDTO> personEntities) {
        return conventService.saveAll(personEntities);
    }

    @GetMapping("persons")
    public List<ConventDTO> getPersons() {
        return conventService.findAll();
    }

    @GetMapping("person/{id}")
    public ResponseEntity<ConventDTO> getPerson(@PathVariable String id) {
        ConventDTO conventDTO = conventService.findOne(id);
        if (conventDTO == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok(conventDTO);
    }

    @GetMapping("persons/{ids}")
    public List<ConventDTO> getPersons(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return conventService.findAll(listIds);
    }

    @GetMapping("persons/count")
    public Long getCount() {
        return conventService.count();
    }

    @DeleteMapping("person/{id}")
    public Long deletePerson(@PathVariable String id) {
        return conventService.delete(id);
    }

    @DeleteMapping("persons/{ids}")
    public Long deletePersons(@PathVariable String ids) {
        List<String> listIds = List.of(ids.split(","));
        return conventService.delete(listIds);
    }

    @DeleteMapping("persons")
    public Long deletePersons() {
        return conventService.deleteAll();
    }

    @PutMapping("person")
    public ConventDTO putPerson(@RequestBody ConventDTO conventDTO) {
        return conventService.update(conventDTO);
    }

    @PutMapping("persons")
    public Long putPerson(@RequestBody List<ConventDTO> conventDTOS) {
        return conventService.update(conventDTOS);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final Exception handleAllExceptions(RuntimeException e) {
        LOGGER.error("Internal server error.", e);
        return e;
    }
}