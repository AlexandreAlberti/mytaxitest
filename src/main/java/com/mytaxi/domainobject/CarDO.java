package com.mytaxi.domainobject;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.mytaxi.domainvalue.EngineType;

@Entity
@Table(
    name = "car",
    uniqueConstraints = @UniqueConstraint(name = "uc_car_licensePlate", columnNames = {"LICENSE_PLATE"}))
public class CarDO
{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "LICENSE_PLATE")
    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    @Column(nullable = false, name = "SEAT_COUNT")
    @NotNull(message = "Number of seats can not be null!")
    @Min(value = 2) // For the driver and, at lesast, 1 passenger
    private Integer seatCount;

    @Column(nullable = true)
    private Boolean convertible = false;

    @Column(nullable = true)
    @Min(value = 0)
    private Float rating = 0.0F;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "ENGINE_TYPE")
    private EngineType engineType;

    @ManyToOne
    @JoinColumn(nullable = true, name = "MANUFACTURER_ID")
    private ManufacturerDO manufacturer;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    private CarDO()
    {}


    public CarDO(Long id, String licensePlate, Integer seatCount, Boolean convertible, Float rating, EngineType engineType, ManufacturerDO manufacturer)
    {
        super();
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    }


    public Long getId()
    {
        return id;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public Float getRating()
    {
        return rating;
    }


    public EngineType getEngineType()
    {
        return engineType;
    }


    public ManufacturerDO getManufacturer()
    {
        return manufacturer;
    }

}
