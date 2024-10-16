package com.glofox.controller;

import com.glofox.model.Class;
import com.glofox.model.ErrorResponse;
import com.glofox.service.ClassService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling class creation, retrieval, update, and deletion.
 */
@RestController
@RequestMapping("/classes")
public class ClassController {
    @Autowired
    private ClassService classService;

    @PostMapping
    public ResponseEntity<?> createClass(@RequestBody Class newClass) {
        try {
        	
            Class createdClass = classService.createClass(newClass);
            return new ResponseEntity<>(createdClass, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Class>> getAllClasses() {
        List<Class> classes = classService.getAllClasses();
        
        if (classes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(classes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Long id,@RequestBody Class updatedClass) {
        try {
            Class updated = classService.updateClass(id, updatedClass);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }


}
