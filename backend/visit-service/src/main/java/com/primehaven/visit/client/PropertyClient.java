package com.primehaven.visit.client;
import org.springframework.cloud.openfeign.FeignClient; import org.springframework.web.bind.annotation.*;
@FeignClient(name="property-service") public interface PropertyClient { @GetMapping("/api/properties/{id}") Object getProperty(@PathVariable Long id); }
