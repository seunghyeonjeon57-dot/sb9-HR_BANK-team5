package com.example.hrbank.domain.employee.repository.Impl;

import static com.example.hrbank.domain.department.entity.QDepartment.department;

import com.example.hrbank.domain.employee.dto.request.EmployeeSearchRequest;
import com.example.hrbank.domain.employee.dto.request.EmployeeUpdateRequest;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.QEmployee;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import com.example.hrbank.domain.employee.repository.custom.ChangeLogRepositoryCustom;
import com.example.hrbank.domain.employee.repository.custom.EmployeeRepositoryCustom;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;


@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
  private final JPAQueryFactory factory;
  private final QEmployee employee;

  @Override
  public List<Employee> searchEmployee(EmployeeSearchRequest request) {
    return factory.selectFrom(employee)
        .leftJoin(employee.department, department).fetchJoin()
        .where(
            containsNameOrEmail(request.nameOrEmail()),
            containsDepartmentName(request.departmentName()),
            containsPosition(request.position()),
            containsEmployeeNumber(request.employeeNumber()),
            eqEmployeeStatus(request.status()),
            betweenHireDate(request.hireDateFrom(),request.hireDateTo()),
            pagingCondition(request.idAfter(),request.sortDirection())
        )
        .orderBy(getSortOrder(request.sortField(),request.sortDirection()))
        .limit(request.size()+1)
        .fetch();
  }

  @Override
  public Long countEmployee(EmployeeSearchRequest request) {
    return factory.select(employee.count())
        .from(employee)
        .leftJoin(employee.department,department)
        .where(containsNameOrEmail(request.nameOrEmail()),
            containsDepartmentName(request.departmentName()),
            containsPosition(request.position()),
            containsEmployeeNumber(request.employeeNumber()),
            eqEmployeeStatus(request.status()),
            betweenHireDate(request.hireDateFrom(), request.hireDateTo()))
        .fetchOne();
  }




  private BooleanExpression containsNameOrEmail(String keyword){
    if(!StringUtils.hasText(keyword)) return null;
    return employee.name.contains(keyword).or(employee.email.contains(keyword
    ));
  }
  private BooleanExpression containsDepartmentName(String keyword){
    if(!StringUtils.hasText(keyword))return null;
    return employee.department.name.contains(keyword);
  }
  private BooleanExpression containsPosition(String keyword){
    if(!StringUtils.hasText(keyword)) return null;
    return employee.position.contains(keyword);
  }
  private BooleanExpression containsEmployeeNumber(String keyword){
    if(!StringUtils.hasText(keyword))return null;
    return employee.employeeNumber.contains(keyword);
  }
  private BooleanExpression eqEmployeeStatus(EmployeeStatus status){
    if(status==null) return null;
    return employee.status.eq(status);
  }
  private BooleanExpression betweenHireDate(LocalDate from,LocalDate to){
    if(from!=null && to!=null){
      return employee.hireDate.between(from,to);
    }
    if(from != null){
      return employee.hireDate.goe(from);
    }
    if(to!=null){
      return employee.hireDate.loe(to);
    }
    return null;
  }
  private BooleanExpression ltEmployeeId(Long lastId){
    if(lastId==null) return null;
    return employee.id.lt(lastId);
  }
  private BooleanExpression pagingCondition(Long lastId, String direction) {
    if (lastId == null) return null;
    return "DESC".equalsIgnoreCase(direction) ? employee.id.lt(lastId) : employee.id.gt(lastId);
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

    // 팩트: 중복 값 대비 보조 정렬(id)을 추가해야 페이징이 안 꼬입니다.
    return new OrderSpecifier[]{mainOrder, new OrderSpecifier<>(order, employee.id)};
  }

}
