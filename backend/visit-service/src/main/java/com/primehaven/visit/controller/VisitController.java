package com.primehaven.visit.controller;
import com.primehaven.visit.client.PropertyClient; import com.primehaven.visit.entity.Visit; import com.primehaven.visit.repository.VisitRepository; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/visits") public class VisitController {
 private final VisitRepository repo; private final PropertyClient properties; public VisitController(VisitRepository repo,PropertyClient properties){this.repo=repo;this.properties=properties;}
 @PostMapping public ResponseEntity<Visit> create(@Valid @RequestBody Visit visit){properties.getProperty(visit.getPropertyId()); return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(visit));}
 @GetMapping public List<Visit> all(){return repo.findAll();} @GetMapping("/{id}") public Visit one(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new NoSuchElementException("Visit not found"));}
 @GetMapping("/property/{propertyId}") public List<Visit> property(@PathVariable Long propertyId){return repo.findByPropertyId(propertyId);} @GetMapping("/customer/{customerId}") public List<Visit> customer(@PathVariable Long customerId){return repo.findByCustomerId(customerId);}
 @PutMapping("/{id}/status") public Visit status(@PathVariable Long id,@RequestParam String value){Visit v=one(id);v.setStatus(value.toUpperCase());return repo.save(v);}
}
