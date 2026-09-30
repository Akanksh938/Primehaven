package com.primehaven.property.controller;
import com.primehaven.property.entity.Property;
import com.primehaven.property.repository.PropertyRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController @RequestMapping("/api/properties")
public class PropertyController {
  private final PropertyRepository repository;
  public PropertyController(PropertyRepository repository){this.repository=repository;}
  @PostMapping public ResponseEntity<Property> create(@Valid @RequestBody Property property){return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(property));}
  @GetMapping public List<Property> all(){return repository.findAll();}
  @GetMapping("/{id}") public Property one(@PathVariable Long id){return repository.findById(id).orElseThrow(()->new NoSuchElementException("Property not found: "+id));}
  @PutMapping("/{id}") public Property update(@PathVariable Long id,@Valid @RequestBody Property body){body.setId(id); if(!repository.existsById(id)) throw new NoSuchElementException("Property not found: "+id); return repository.save(body);}
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){repository.deleteById(id);}
  @GetMapping("/search") public List<Property> search(@RequestParam(defaultValue="") String city,@RequestParam(defaultValue="") String type,@RequestParam(required=false) BigDecimal minPrice,@RequestParam(required=false) BigDecimal maxPrice){return repository.findByCityContainingIgnoreCaseAndTypeContainingIgnoreCase(city,type).stream().filter(p->minPrice==null||p.getPrice().compareTo(minPrice)>=0).filter(p->maxPrice==null||p.getPrice().compareTo(maxPrice)<=0).toList();}
}
