package com.glofox.TestControllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.glofox.controller.ClassController;
import com.glofox.model.Class;
import com.glofox.model.ErrorResponse;
import com.glofox.service.ClassService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ClassControllerTest {

    @Mock
    private ClassService classService;

    @InjectMocks
    private ClassController classController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClass_Success() {
        Class newClass = new Class();
        newClass.setId(1L); // Set an ID for the created class
        when(classService.createClass(any(Class.class))).thenReturn(newClass);

        ResponseEntity<Object> responseEntity = classController.createClass(newClass);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(newClass, responseEntity.getBody());
        verify(classService, times(1)).createClass(any(Class.class));
    }

    @Test
    void testCreateClass_Failure() {
        String errorMessage = "Invalid class data";
        when(classService.createClass(any(Class.class))).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Object> responseEntity = classController.createClass(new Class());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
    }

    @Test
    void testGetAllClasses_NotEmpty() {
        List<Class> classes = Arrays.asList(new Class(), new Class());
        when(classService.getAllClasses()).thenReturn(classes);

        ResponseEntity<List<Class>> responseEntity = classController.getAllClasses();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classes, responseEntity.getBody());
        verify(classService, times(1)).getAllClasses();
    }

    @Test
    void testGetAllClasses_Empty() {
        when(classService.getAllClasses()).thenReturn(Arrays.asList());

        ResponseEntity<List<Class>> responseEntity = classController.getAllClasses();

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
        verify(classService, times(1)).getAllClasses();
    }

    @Test
    void testUpdateClass_Success() {
        Long classId = 1L;
        Class updatedClass = new Class();
        updatedClass.setId(classId);
        when(classService.updateClass(eq(classId), any(Class.class))).thenReturn(updatedClass);

        ResponseEntity<Object> responseEntity = classController.updateClass(classId, updatedClass);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedClass, responseEntity.getBody());
        verify(classService, times(1)).updateClass(eq(classId), any(Class.class));
    }

    @Test
    void testUpdateClass_Failure() {
        Long classId = 1L;
        String errorMessage = "Class not found";
        when(classService.updateClass(eq(classId), any(Class.class))).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Object> responseEntity = classController.updateClass(classId, new Class());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(errorMessage, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
    }
}
