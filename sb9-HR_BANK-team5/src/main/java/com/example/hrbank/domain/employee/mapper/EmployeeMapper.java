package com.example.hrbank.domain.employee.mapper;


import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.binarycontent.mapper.BinaryContentMapper;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.department.mapper.DepartmentMapper;
import com.example.hrbank.domain.employee.dto.data.CursorPageResponseEmployeeDto;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.entity.Employee;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses={BinaryContentMapper.class, DepartmentMapper.class})
public interface EmployeeMapper {
  @Mapping(source="department.id",target = "departmentId")
  @Mapping(source = "profileImage.id",target="profileImageId")
  @Mapping(source="department.name",target="departmentName")
  EmployeeDto toDto(Employee employee);


  List<EmployeeDto> toDtoList(List<Employee> entities);

  default CursorPageResponseEmployeeDto toCursorPageResponse(
      List<Employee> entities,
      long totalElements,
      boolean hasNext,
      int size
  ){
    List<EmployeeDto> content = toDtoList(entities);
    Long lastId = entities.isEmpty() ? null : entities.get(entities.size()-1).getId();
    return new CursorPageResponseEmployeeDto(
        content,
        lastId != null ? lastId.toString():null,
        lastId,
        size,
        totalElements,
        hasNext
    );
  }
}
