package com.example.hrbank.domain.department.mapper;

import com.example.hrbank.domain.department.dto.data.DepartmentDto;
import com.example.hrbank.domain.department.entity.Department;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-16T13:49:20+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 17.0.18 (Oracle Corporation)"
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

    @Override
    public List<DepartmentDto> toDtoList(List<Department> entities) {
        if ( entities == null ) {
            return null;
        }

        List<DepartmentDto> list = new ArrayList<DepartmentDto>( entities.size() );
        for ( Department department : entities ) {
            list.add( toDto( department ) );
        }

        return list;
    }
}
