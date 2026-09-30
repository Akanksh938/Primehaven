package com.primehaven.property;

import com.primehaven.property.entity.Property;
import com.primehaven.property.repository.PropertyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class PropertyDataSeeder {

    @Bean
    CommandLineRunner seedProperties(PropertyRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            repository.saveAll(List.of(
                createProperty(
                    "Modern 3 BHK Apartment",
                    "Bright apartment near the metro with a balcony and a modern kitchen.",
                    new BigDecimal("8500000"),
                    "APARTMENT",
                    "12 Palm Avenue",
                    "Bengaluru",
                    3,
                    2,
                    1450,
                    "https://placehold.co/640x400?text=3+BHK",
                    1L
                ),
                createProperty(
                    "Lake View Villa",
                    "Spacious villa with a private garden and a peaceful lake view.",
                    new BigDecimal("18500000"),
                    "VILLA",
                    "88 Lake Road",
                    "Hyderabad",
                    4,
                    3,
                    2400,
                    "https://placehold.co/640x400?text=Villa",
                    2L
                ),
                createProperty(
                    "Cozy Studio Flat",
                    "Compact and well-lit studio suitable for working professionals.",
                    new BigDecimal("4200000"),
                    "STUDIO",
                    "21 Market Lane",
                    "Pune",
                    1,
                    1,
                    600,
                    "https://placehold.co/640x400?text=Studio",
                    3L
                )
            ));
        };
    }

    private Property createProperty(
        String title,
        String description,
        BigDecimal price,
        String type,
        String address,
        String city,
        int bedrooms,
        int bathrooms,
        int areaSqFt,
        String imageUrl,
        Long ownerId
    ) {
        Property property = new Property();
        property.setTitle(title);
        property.setDescription(description);
        property.setPrice(price);
        property.setType(type);
        property.setStatus("AVAILABLE");
        property.setAddress(address);
        property.setCity(city);
        property.setBedrooms(bedrooms);
        property.setBathrooms(bathrooms);
        property.setAreaSqFt(areaSqFt);
        property.setImageUrl(imageUrl);
        property.setOwnerId(ownerId);
        return property;
    }
}
