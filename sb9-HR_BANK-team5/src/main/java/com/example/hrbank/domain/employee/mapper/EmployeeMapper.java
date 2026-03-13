package com.example.hrbank.domain.employee.mapper;


import com.example.hrbank.domain.binarycontent.mapper.BinaryContentMapper;
import com.example.hrbank.domain.department.mapper.DepartmentMapper;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses={BinaryContentMapper.class, DepartmentMapper.class})
public interface EmployeeMapper {
  @Mapping(source="department.id",target = "departmentId")
  @Mapping(source = "profileImage.id",target="profileImageId")
  @Mapping(source="department.name",target="departmentName")
  EmployeeDto toDto(Employee employee);

}
