package com.example.hrbank.domain.department.repository;

import com.example.hrbank.domain.department.entity.Department;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//JpaRepository에는 crud만 있어서 검색 조건이 있는 조회 등이 있는 인터페이스 추가 상속
public interface DepartmentRepository extends JpaRepository<Department, Long>{

  boolean existsByName(String name);

//  입력받은 id보다 큰 id를 가진 Department들을 조회하고, id 기준으로 오름차순 정렬해서 반환한다.
  List<Department> findByIdGreaterThanOrderByIdAsc(Long id, Pageable pageable);

  @Query("""
SELECT d
FROM Department d
WHERE (:keyword IS NULL OR :keyword = ''
       OR d.name LIKE %:keyword%
       OR d.description LIKE %:keyword%)
AND (:idAfter IS NULL OR d.id > :idAfter)
""")
  List<Department> searchDepartments(
      @Param("keyword") String keyword,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );
}
