package com.glofox.service;

import com.glofox.model.Class;
import com.glofox.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class to handle business logic related to classes.
 */
@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    public Class createClass(Class newClass) {
        if (newClass.getStartDate().isAfter(newClass.getEndDate())) {
            throw new RuntimeException("The start date must be on or before the end date.");
        }

        boolean hasOverlap = classRepository.existsOverlappingClasses(newClass.getStartDate(), newClass.getEndDate());
        if (hasOverlap) {
            throw new RuntimeException("Class overlaps with existing classes in the given date range.");
        }

        return classRepository.save(newClass);
    }

    public Class updateClass(Long id, Class updatedClass) {
  
        Class existingClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + id));

        if (updatedClass.getStartDate().isAfter(updatedClass.getEndDate())) {
            throw new RuntimeException("The start date must be on or before the end date.");
        }
        boolean hasOverlap = classRepository.existsOverlappingClasses(updatedClass.getStartDate(), updatedClass.getEndDate());
        if (hasOverlap) {
            throw new RuntimeException("Class overlaps with existing classes in the given date range.");
        }

        existingClass.setName(updatedClass.getName());
        existingClass.setStartDate(updatedClass.getStartDate());
        existingClass.setEndDate(updatedClass.getEndDate());
        existingClass.setCapacity(updatedClass.getCapacity());

        return classRepository.save(existingClass);
    }



    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }
}
