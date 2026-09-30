package com.primehaven.property.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity @Table(name = "properties")
public class Property {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @NotBlank private String title;
  @Column(length = 3000) private String description;
  @NotNull @Positive private BigDecimal price;
  @NotBlank private String type;
  @NotBlank private String status = "AVAILABLE";
  @NotBlank private String address;
  @NotBlank private String city;
  @PositiveOrZero private Integer bedrooms;
  @PositiveOrZero private Integer bathrooms;
  @PositiveOrZero private Integer areaSqFt;
  private String imageUrl;
  private Long ownerId;
  public Long getId(){return id;} public void setId(Long v){id=v;} public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;} public String getType(){return type;} public void setType(String v){type=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public String getAddress(){return address;} public void setAddress(String v){address=v;} public String getCity(){return city;} public void setCity(String v){city=v;} public Integer getBedrooms(){return bedrooms;} public void setBedrooms(Integer v){bedrooms=v;} public Integer getBathrooms(){return bathrooms;} public void setBathrooms(Integer v){bathrooms=v;} public Integer getAreaSqFt(){return areaSqFt;} public void setAreaSqFt(Integer v){areaSqFt=v;} public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;} public Long getOwnerId(){return ownerId;} public void setOwnerId(Long v){ownerId=v;}
}
