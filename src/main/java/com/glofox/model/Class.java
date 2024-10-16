package com.glofox.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a Class.
 */
@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor 
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty("name")
    @NotEmpty(message = "Class name cannot be empty.")
    private String name;  // The name of the class (e.g., Pilates, Yoga)

    @JsonProperty("startDate")
    @NotNull(message = "Start date cannot be null.")
    private LocalDate startDate;  // The start date of the class series

    @JsonProperty("endDate")
    @NotNull(message = "End date cannot be null.")
    private LocalDate endDate;  // The end date of the class series

    @JsonProperty("capacity")
    @NotNull(message = "Capacity cannot be null.")
    private Integer capacity;  // The maximum number of attendees for each class session

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

   
}
