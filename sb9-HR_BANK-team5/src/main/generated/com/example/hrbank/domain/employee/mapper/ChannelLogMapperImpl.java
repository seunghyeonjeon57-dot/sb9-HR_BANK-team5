package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import java.time.LocalDateTime;
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
public class ChannelLogMapperImpl implements ChannelLogMapper {

    @Override
    public ChangeLogDto toDto(ChangeLog changeLog) {
        if ( changeLog == null ) {
            return null;
        }

        Long id = null;
        ChannelType type = null;
        String employeeNumber = null;
        String memo = null;
        String ipAddress = null;
        LocalDateTime at = null;

        id = changeLog.getId();
        type = changeLog.getType();
        employeeNumber = changeLog.getEmployeeNumber();
        memo = changeLog.getMemo();
        ipAddress = changeLog.getIpAddress();
        at = changeLog.getAt();

        ChangeLogDto changeLogDto = new ChangeLogDto( id, type, employeeNumber, memo, ipAddress, at );

        return changeLogDto;
    }

    @Override
    public List<ChangeLogDto> toDtoList(List<ChangeLog> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ChangeLogDto> list = new ArrayList<ChangeLogDto>( entities.size() );
        for ( ChangeLog changeLog : entities ) {
            list.add( toDto( changeLog ) );
        }

        return list;
    }
}
