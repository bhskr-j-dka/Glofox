package com.glofox.TestServices;

import com.glofox.model.Class;
import com.glofox.repository.ClassRepository;
import com.glofox.service.ClassService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    @InjectMocks
    private ClassService classService;

    @Mock
    private ClassRepository classRepository;

    private Class newClass;
    private Class existingClass;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample Class object for creation
        newClass = new Class();
        newClass.setId(1L);
        newClass.setName("Yoga Class");
        newClass.setStartDate(LocalDate.of(2024, 10, 1));
        newClass.setEndDate(LocalDate.of(2024, 10, 10));

        // Initialize a sample existing Class object for updates
        existingClass = new Class();
        existingClass.setId(1L);
        existingClass.setName("Pilates Class");
        existingClass.setStartDate(LocalDate.of(2024, 10, 5));
        existingClass.setEndDate(LocalDate.of(2024, 10, 15));
    }

    @Test
    void testCreateClass_Success() {
        // Mock the behavior of class repository
        when(classRepository.existsOverlappingClasses(any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
        when(classRepository.save(any(Class.class))).thenReturn(newClass);

        Class createdClass = classService.createClass(newClass);

        assertNotNull(createdClass);
        assertEquals(newClass.getId(), createdClass.getId());
        verify(classRepository, times(1)).save(newClass);
    }

    @Test
    void testCreateClass_StartDateAfterEndDate() {
        newClass.setStartDate(LocalDate.of(2024, 10, 15));
        newClass.setEndDate(LocalDate.of(2024, 10, 10));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            classService.createClass(newClass);
        });

        assertEquals("The start date must be on or before the end date.", exception.getMessage());
    }

    @Test
    void testCreateClass_OverlapWithExistingClasses() {
        when(classRepository.existsOverlappingClasses(any(LocalDate.class), any(LocalDate.class))).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            classService.createClass(newClass);
        });

        assertEquals("Class overlaps with existing classes in the given date range.", exception.getMessage());
    }

    @Test
    void testUpdateClass_Success() {
        when(classRepository.findById(1L)).thenReturn(Optional.of(existingClass));
        when(classRepository.save(any(Class.class))).thenReturn(existingClass);

        Class updatedClass = new Class();
        updatedClass.setName("Updated Pilates Class");
        updatedClass.setStartDate(LocalDate.of(2024, 10, 5));
        updatedClass.setEndDate(LocalDate.of(2024, 10, 20));

        Class result = classService.updateClass(1L, updatedClass);

        assertNotNull(result);
        assertEquals("Updated Pilates Class", result.getName());
        verify(classRepository, times(1)).save(existingClass);
    }

    @Test
    void testUpdateClass_ClassNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            classService.updateClass(1L, existingClass);
        });

        assertEquals("Class not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateClass_StartDateAfterEndDate() {
        existingClass.setStartDate(LocalDate.of(2024, 10, 15));
        existingClass.setEndDate(LocalDate.of(2024, 10, 10));

        when(classRepository.findById(1L)).thenReturn(Optional.of(existingClass));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            classService.updateClass(1L, existingClass);
        });

        assertEquals("The start date must be on or before the end date.", exception.getMessage());
    }

    @Test
    void testGetAllClasses() {
        when(classRepository.findAll()).thenReturn(Arrays.asList(existingClass));

        List<Class> classes = classService.getAllClasses();

        assertNotNull(classes);
        assertEquals(1, classes.size());
        assertEquals("Pilates Class", classes.get(0).getName());
    }
}
