package com.primehaven.inquiry.controller;
import com.primehaven.inquiry.entity.Inquiry; import com.primehaven.inquiry.repository.InquiryRepository; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/inquiries") public class InquiryController {
 private final InquiryRepository repo; public InquiryController(InquiryRepository repo){this.repo=repo;}
 @PostMapping public ResponseEntity<Inquiry> create(@Valid @RequestBody Inquiry inquiry){return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(inquiry));}
 @GetMapping public List<Inquiry> all(){return repo.findAll();}
 @GetMapping("/{id}") public Inquiry one(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new NoSuchElementException("Inquiry not found"));}
 @GetMapping("/property/{propertyId}") public List<Inquiry> byProperty(@PathVariable Long propertyId){return repo.findByPropertyId(propertyId);}
 @PutMapping("/{id}/status") public Inquiry setStatus(@PathVariable Long id,@RequestParam String value){Inquiry i=one(id); i.setStatus(value.toUpperCase()); return repo.save(i);}
}
