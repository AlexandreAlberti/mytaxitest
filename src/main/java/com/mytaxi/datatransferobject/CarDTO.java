package com.mytaxi.datatransferobject;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytaxi.domainvalue.EngineType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO
{

    @JsonIgnore
    private Long id;

    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    @NotNull(message = "Number of seats can not be null!")
    private Integer seatCount;

    private Boolean convertible = false;

    private Float rating = 0.0F;

    private EngineType engineType;

    private Long manufacturerId;
    private String manufacturerName;


    private CarDTO()
    {}


    private CarDTO(Long id, String licensePlate, Integer seatCount, Boolean convertible, Float rating, EngineType engineType, Long manufacturerId, String manufacturerName)
    {
        super();
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturerId = manufacturerId;
        this.manufacturerName = manufacturerName;
    }


    public static CarDTOBuilder newBuilder()
    {
        return new CarDTOBuilder();
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


    public Long getManufacturerId()
    {
        return manufacturerId;
    }


    public String getManufacturerName()
    {
        return manufacturerName;
    }

    public static class CarDTOBuilder
    {
        private Long id;
        private String licensePlate;
        private Integer seatCount;
        private Boolean convertible = false;
        private Float rating = 0.0F;
        private EngineType engineType;
        private Long manufacturerId;
        private String manufacturerName;


        public CarDTOBuilder setId(Long id)
        {
            this.id = id;
            return this;
        }


        public CarDTOBuilder setLicensePlate(String licensePlate)
        {
            this.licensePlate = licensePlate;
            return this;
        }


        public CarDTOBuilder setSeatCount(Integer seatCount)
        {
            this.seatCount = seatCount;
            return this;
        }


        public CarDTOBuilder setConvertible(Boolean convertible)
        {
            this.convertible = convertible;
            return this;
        }


        public CarDTOBuilder setRating(Float rating)
        {
            this.rating = rating;
            return this;
        }


        public CarDTOBuilder setEngineType(EngineType engineType)
        {
            this.engineType = engineType;
            return this;
        }


        public CarDTOBuilder setManufacturerId(Long manufacturerId)
        {
            this.manufacturerId = manufacturerId;
            return this;
        }


        public CarDTOBuilder setManufacturerName(String manufacturerName)
        {
            this.manufacturerName = manufacturerName;
            return this;
        }


        public CarDTO createCarDTO()
        {
            return new CarDTO(id, licensePlate, seatCount, convertible, rating, engineType, manufacturerId, manufacturerName);
        }

    }


    public String toJsonString()
    {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"licensePlate\":\"");
        sb.append(licensePlate);
        sb.append("\", ");
        sb.append("\"seatCount\":");
        sb.append(seatCount);

        if (id != null && id.longValue() != 0L)
        {
            sb.append(", ");
            sb.append("\"id\":");
            sb.append(id);
        }
        if (convertible != null)
        {
            sb.append(", ");
            sb.append("\"convertible\":");
            sb.append(convertible);
        }
        if (rating != null)
        {
            sb.append(", ");
            sb.append("\"rating\":");
            sb.append(rating);
        }
        if (engineType != null)
        {
            sb.append(", ");
            sb.append("\"engineType\":\"");
            sb.append(engineType);
            sb.append("\"");
        }
        if (manufacturerId != null)
        {
            sb.append(", ");
            sb.append("\"manufacturerId\":");
            sb.append(manufacturerId);
        }
        sb.append("}");
        return sb.toString();
    }
}
