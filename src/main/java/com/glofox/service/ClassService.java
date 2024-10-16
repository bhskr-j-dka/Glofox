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
    	validateClass(newClass);


        return classRepository.save(newClass);
    }

    public Class updateClass(Long id, Class updatedClass) {
  
        Class existingClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with ID: " + id));

         validateClass(updatedClass);

        existingClass.setName(updatedClass.getName());
        existingClass.setStartDate(updatedClass.getStartDate());
        existingClass.setEndDate(updatedClass.getEndDate());
        existingClass.setCapacity(updatedClass.getCapacity());

        return classRepository.save(existingClass);
    }



    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }
    
    private void validateClass(Class clazz) {
        if (clazz.getName() == null || clazz.getName().isEmpty()) {
            throw new RuntimeException("Class name cannot be empty.");
        }
        if (clazz.getStartDate() == null) {
            throw new RuntimeException("Start date cannot be null.");
        }
        if (clazz.getEndDate() == null) {
            throw new RuntimeException("End date cannot be null.");
        }
        if (clazz.getCapacity() == null || clazz.getCapacity() <= 0) {
            throw new RuntimeException("Capacity must be a positive integer.");
        }
        if (clazz.getStartDate().isAfter(clazz.getEndDate())) {
            throw new RuntimeException("The start date must be on or before the end date.");
        }
        boolean hasOverlap = classRepository.existsOverlappingClasses(clazz.getStartDate(), clazz.getEndDate());
        if (hasOverlap) {
            throw new RuntimeException("Class overlaps with existing classes in the given date range.");
        }
    
}
}
