package com.primehaven.property.repository;
import com.primehaven.property.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PropertyRepository extends JpaRepository<Property, Long> { List<Property> findByCityContainingIgnoreCaseAndTypeContainingIgnoreCase(String city, String type); }
