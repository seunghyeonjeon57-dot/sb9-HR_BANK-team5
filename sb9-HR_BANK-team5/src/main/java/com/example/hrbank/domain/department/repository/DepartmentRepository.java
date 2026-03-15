package com.example.hrbank.domain.department.repository;

import com.example.hrbank.domain.department.entity.Department;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

  boolean existsByName(String name);

//  입력받은 id보다 큰 id를 가진 Department들을 조회하고, id 기준으로 오름차순 정렬해서 반환한다.
  List<Department> findByIdGreaterThanOrderByIdAsc(Long id, Pageable pageable);

}
