package com.example.hrbank.domain.department.mapper;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.entity.Department;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-13T22:12:11+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 17.0.17 (Amazon.com Inc.)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public DepartmentDto toDto(Department department) {
        if ( department == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;
        LocalDate establishedDate = null;
        Integer employeeCount = null;

        id = department.getId();
        name = department.getName();
        description = department.getDescription();
        establishedDate = department.getEstablishedDate();
        employeeCount = department.getEmployeeCount();

        DepartmentDto departmentDto = new DepartmentDto( id, name, description, establishedDate, employeeCount );

        return departmentDto;
    }
}
