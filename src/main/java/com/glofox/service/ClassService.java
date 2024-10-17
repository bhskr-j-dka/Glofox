package com.glofox.service;

import com.glofox.model.Class;
import com.glofox.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public Class createClass(Class newClass) {
        // Validate the new class without excluding any ID (for creation)
        validateClass(newClass, null);
        return classRepository.save(newClass);
    }

    public Class updateClass(Long id, Class updatedClass) {
        Class existingClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + id));

        // Update the fields of the existing class with new values
        existingClass.setName(updatedClass.getName());
        existingClass.setStartDate(updatedClass.getStartDate());
        existingClass.setEndDate(updatedClass.getEndDate());
        existingClass.setCapacity(updatedClass.getCapacity());

        // Validate the updated class, excluding its own ID to avoid false overlap detection
        validateClass(existingClass, id);

        return classRepository.save(existingClass);
    }

    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }
    
    private void validateClass(Class classToValidate, Long classIdToExclude) {
        if (classToValidate.getName() == null || classToValidate.getName().isEmpty()) {
            throw new RuntimeException("Class name cannot be empty.");
        }
        if (classToValidate.getStartDate() == null) {
            throw new RuntimeException("Start date cannot be null.");
        }
        if (classToValidate.getEndDate() == null) {
            throw new RuntimeException("End date cannot be null.");
        }
        if (classToValidate.getCapacity() == null || classToValidate.getCapacity() <= 0) {
            throw new RuntimeException("Capacity must be a positive integer.");
        }
        if (classToValidate.getStartDate().isAfter(classToValidate.getEndDate())) {
            throw new RuntimeException("The start date must be on or before the end date.");
        }

        // Check for overlapping classes, excluding the specified class ID (for updates).
        boolean hasOverlap = classRepository.existsOverlappingClasses(
        		classToValidate.getStartDate(),
        		classToValidate.getEndDate(),
                classIdToExclude
        );
        if (hasOverlap) {
            throw new RuntimeException("Class overlaps with existing classes in the given date range.");
        }
    }
}
