package com.glofox.TestRepositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.glofox.model.Class;
import com.glofox.repository.ClassRepository;

@ExtendWith(MockitoExtension.class)
class ClassRepositoryTest {

    @Mock
    private ClassRepository classRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExistsOverlappingClasses_True() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 10);

        // Mocking the behavior of the repository
        when(classRepository.existsOverlappingClasses(startDate, endDate)).thenReturn(true);

        boolean result = classRepository.existsOverlappingClasses(startDate, endDate);

        assertTrue(result, "Expected overlapping classes to exist");
    }

    @Test
    void testExistsOverlappingClasses_False() {
        LocalDate startDate = LocalDate.of(2024, 10, 11);
        LocalDate endDate = LocalDate.of(2024, 10, 20);

        // Mocking the behavior of the repository
        when(classRepository.existsOverlappingClasses(startDate, endDate)).thenReturn(false);

        boolean result = classRepository.existsOverlappingClasses(startDate, endDate);

        assertFalse(result, "Expected no overlapping classes to exist");
    }
}
