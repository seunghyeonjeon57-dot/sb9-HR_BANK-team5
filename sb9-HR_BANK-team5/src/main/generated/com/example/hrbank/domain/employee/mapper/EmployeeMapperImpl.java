package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.department.entity.Department;
import com.example.hrbank.domain.employee.dto.data.EmployeeDto;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.EmployeeStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-13T16:37:49+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 17.0.17 (Amazon.com Inc.)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDto toDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        Long departmentId = null;
        Long profileImageId = null;
        String departmentName = null;
        Long id = null;
        String name = null;
        String email = null;
        String employeeNumber = null;
        String position = null;
        LocalDate hireDate = null;
        EmployeeStatus status = null;

        departmentId = employeeDepartmentId( employee );
        profileImageId = employeeProfileImageId( employee );
        departmentName = employeeDepartmentName( employee );
        id = employee.getId();
        name = employee.getName();
        email = employee.getEmail();
        employeeNumber = employee.getEmployeeNumber();
        position = employee.getPosition();
        hireDate = employee.getHireDate();
        status = employee.getStatus();

        EmployeeDto employeeDto = new EmployeeDto( id, name, email, employeeNumber, departmentId, departmentName, position, hireDate, status, profileImageId );

        return employeeDto;
    }

    @Override
    public List<EmployeeDto> toDtoList(List<Employee> entities) {
        if ( entities == null ) {
            return null;
        }

        List<EmployeeDto> list = new ArrayList<EmployeeDto>( entities.size() );
        for ( Employee employee : entities ) {
            list.add( toDto( employee ) );
        }

        return list;
    }

    private Long employeeDepartmentId(Employee employee) {
        if ( employee == null ) {
            return null;
        }
        Department department = employee.getDepartment();
        if ( department == null ) {
            return null;
        }
        Long id = department.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long employeeProfileImageId(Employee employee) {
        if ( employee == null ) {
            return null;
        }
        BinaryContent profileImage = employee.getProfileImage();
        if ( profileImage == null ) {
            return null;
        }
        Long id = profileImage.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String employeeDepartmentName(Employee employee) {
        if ( employee == null ) {
            return null;
        }
        Department department = employee.getDepartment();
        if ( department == null ) {
            return null;
        }
        String name = department.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
