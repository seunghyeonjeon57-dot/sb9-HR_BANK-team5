package com.example.hrbank.domain.employee.repository;

import static com.example.hrbank.domain.department.entity.QDepartment.department;
import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.QEmployee;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
  private final JPAQueryFactory factory;
  private final QEmployee employee = QEmployee.employee;

  @Override
  public List<Employee> totalEmployee(EmployeeSearchRequest request, String lastValue, Long lastId) {
    String sortField = request.sortField() != null ? request.sortField() : "id";
    String direction = request.sortDirection() != null ? request.sortDirection() : "asc";

    return factory.selectFrom(employee)
        .leftJoin(employee.department, department).fetchJoin()
        .where(
            containsNameOrEmail(request.nameOrEmail()),
            containsDepartmentName(request.departmentName()),
            containsPosition(request.position()),
            containsEmployeeNumber(request.employeeNumber()),
            eqEmployeeStatus(request.status()),
            betweenHireDate(request.hireDateFrom(), request.hireDateTo()),
            
            compositeCursorCondition(lastValue, lastId, sortField, direction)
        )
        .orderBy(getSortOrder(sortField, direction))
        .limit(request.size() + 1)
        .fetch();
  }

  @Override
  public Long totalCountEmployee(EmployeeSearchRequest request) {
    return factory.select(employee.count())
        .from(employee)
        .leftJoin(employee.department, department)
        .where(
            containsNameOrEmail(request.nameOrEmail()),
            containsDepartmentName(request.departmentName()),
            containsPosition(request.position()),
            containsEmployeeNumber(request.employeeNumber()),
            eqEmployeeStatus(request.status()),
            betweenHireDate(request.hireDateFrom(), request.hireDateTo())
        )
        .fetchOne();
  }

  
  private BooleanExpression compositeCursorCondition(String lastValue, Long lastId, String sortField, String direction) {
    if (lastValue == null || lastId == null) return null;

    boolean isAsc = "asc".equalsIgnoreCase(direction);

    
    StringExpression targetField = switch (sortField) {
      case "name" -> employee.name;
      case "employeeNumber" -> employee.employeeNumber;
      case "hireDate" -> employee.hireDate.stringValue();
      default -> employee.id.stringValue();
    };

    if (isAsc) {
      return targetField.gt(lastValue)
          .or(targetField.eq(lastValue).and(employee.id.gt(lastId)));
    } else {
      return targetField.lt(lastValue)
          .or(targetField.eq(lastValue).and(employee.id.gt(lastId))); 
    }
  }

  private BooleanExpression containsNameOrEmail(String keyword) {
    if (!StringUtils.hasText(keyword)) return null;
    return employee.name.contains(keyword).or(employee.email.contains(keyword));
  }

  private BooleanExpression containsDepartmentName(String keyword) {
    if (!StringUtils.hasText(keyword)) return null;
    return employee.department.name.contains(keyword);
  }

  private BooleanExpression containsPosition(String keyword) {
    if (!StringUtils.hasText(keyword)) return null;
    return employee.position.contains(keyword);
  }

  private BooleanExpression containsEmployeeNumber(String keyword) {
    if (!StringUtils.hasText(keyword)) return null;
    return employee.employeeNumber.contains(keyword);
  }

  private BooleanExpression eqEmployeeStatus(EmployeeStatus status) {
    return status != null ? employee.status.eq(status) : null;
  }

  private BooleanExpression betweenHireDate(LocalDate from, LocalDate to) {
    if (from != null && to != null) return employee.hireDate.between(from, to);
    if (from != null) return employee.hireDate.goe(from);
    if (to != null) return employee.hireDate.loe(to);
    return null;
  }

  private OrderSpecifier<?>[] getSortOrder(String field, String direction) {
    com.querydsl.core.types.Order order = "DESC".equalsIgnoreCase(direction) ?
        com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC;

    OrderSpecifier<?> mainOrder = switch (field != null ? field : "id") {
      case "name" -> new OrderSpecifier<>(order, employee.name);
      case "employeeNumber" -> new OrderSpecifier<>(order, employee.employeeNumber);
      case "hireDate" -> new OrderSpecifier<>(order, employee.hireDate);
      default -> new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, employee.id);
    };

    
    return new OrderSpecifier[]{mainOrder, new OrderSpecifier<>(com.querydsl.core.types.Order.ASC, employee.id)};
  }
}