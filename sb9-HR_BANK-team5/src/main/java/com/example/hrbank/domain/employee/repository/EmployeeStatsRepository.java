package com.example.hrbank.domain.employee.repository;

import com.example.hrbank.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatsRepository extends JpaRepository<Employee,Long>,
    EmployeeStatsRepositoryCustom {


}
