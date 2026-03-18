package com.example.hrbank.domain.department.repository;

import com.example.hrbank.domain.department.entity.Department;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department, Long>{

  boolean existsByName(String name);

  @Query("""
    SELECT d FROM Department d
    WHERE (:keyword IS NULL OR :keyword = '' OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%)
    AND (
      :lastValue IS NULL OR 
      (:direction = 'asc' AND (
        (CASE 
          WHEN :sortField = 'name' THEN d.name 
          WHEN :sortField = 'establishedDate' THEN CAST(d.establishedDate AS string) 
          ELSE CAST(d.id AS string) END > :lastValue)
        OR 
        (CASE 
          WHEN :sortField = 'name' THEN d.name 
          WHEN :sortField = 'establishedDate' THEN CAST(d.establishedDate AS string) 
          ELSE CAST(d.id AS string) END = :lastValue AND d.id > :lastId)
      ))
      OR (:direction = 'desc' AND (
        (CASE 
          WHEN :sortField = 'name' THEN d.name 
          WHEN :sortField = 'establishedDate' THEN CAST(d.establishedDate AS string) 
          ELSE CAST(d.id AS string) END < :lastValue)
        OR 
        (CASE 
          WHEN :sortField = 'name' THEN d.name 
          WHEN :sortField = 'establishedDate' THEN CAST(d.establishedDate AS string) 
          ELSE CAST(d.id AS string) END = :lastValue AND d.id > :lastId)
      ))
    )
    """)
  List<Department> searchDepartments(
      @Param("keyword") String keyword,
      @Param("lastValue") String lastValue,
      @Param("lastId") Long lastId,
      @Param("sortField") String sortField,
      @Param("direction") String direction,
      Pageable pageable
  );
}