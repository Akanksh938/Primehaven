package com.primehaven.visit.entity;
import jakarta.persistence.*; import jakarta.validation.constraints.*; import java.time.LocalDateTime;
@Entity @Table(name="visits") public class Visit {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @NotNull private Long propertyId; @NotNull private Long customerId; @NotNull @Future private LocalDateTime visitDateTime; private String status="REQUESTED"; @Column(length=1000) private String notes;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getPropertyId(){return propertyId;} public void setPropertyId(Long v){propertyId=v;} public Long getCustomerId(){return customerId;} public void setCustomerId(Long v){customerId=v;} public LocalDateTime getVisitDateTime(){return visitDateTime;} public void setVisitDateTime(LocalDateTime v){visitDateTime=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
}
