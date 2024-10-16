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
 * Entity representing a Booking.
 */
@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor 
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty("name")
    @NotEmpty(message = "Member name cannot be empty.")
    private String name;  // The name of the member making the booking

    @JsonProperty("date")
    @NotNull(message = "Booking date cannot be null.")
    private LocalDate date;  // The date for which the member wants to book a class

    @JsonProperty("classId")
    @NotNull(message = "Class ID cannot be null.")
    private Long classId;  // The ID of the class being booked

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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

    // Getter and Setter methods
  
}
