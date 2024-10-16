package com.glofox.repository;

import com.glofox.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

/**
 * Repository interface for Class entity.
 * It provides CRUD operations and a custom query for overlapping date ranges.
 */
public interface ClassRepository extends JpaRepository<Class, Long> {
    /**
     * Finds classes that overlap with the given date range.
     * 
     * @param startDate The start date to check against.
     * @param endDate The end date to check against.
     */
	 @Query("SELECT COUNT(c) > 0 FROM Class c WHERE c.startDate <= :endDate AND c.endDate >= :startDate")
	 boolean existsOverlappingClasses(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
  
  
    

}
