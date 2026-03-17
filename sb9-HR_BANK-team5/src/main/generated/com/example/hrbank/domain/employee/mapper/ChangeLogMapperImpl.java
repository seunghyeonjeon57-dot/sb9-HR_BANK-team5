package com.example.hrbank.domain.employee.mapper;

import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import com.example.hrbank.domain.employee.dto.data.ChangeLogDetailDto;
import com.example.hrbank.domain.employee.dto.data.ChangeLogDto;
import com.example.hrbank.domain.employee.dto.data.DiffDto;
import com.example.hrbank.domain.employee.entity.ChangeLog;
import com.example.hrbank.domain.employee.entity.ChannelDiff;
import com.example.hrbank.domain.employee.entity.Employee;
import com.example.hrbank.domain.employee.entity.enums.ChannelType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-17T13:28:13+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 17.0.17 (Amazon.com Inc.)"
)
@Component
public class ChangeLogMapperImpl implements ChangeLogMapper {

    @Override
    public ChangeLogDto toDto(ChangeLog changeLog) {
        if ( changeLog == null ) {
            return null;
        }

        LocalDateTime at = null;
        Long id = null;
        ChannelType type = null;
        String employeeNumber = null;
        String memo = null;
        String ipAddress = null;

        at = changeLog.getCreatedAt();
        id = changeLog.getId();
        type = changeLog.getType();
        employeeNumber = changeLog.getEmployeeNumber();
        memo = changeLog.getMemo();
        ipAddress = changeLog.getIpAddress();

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

    @Override
    public ChangeLogDetailDto toDetailDto(ChangeLog entity, Employee employee) {
        if ( entity == null && employee == null ) {
            return null;
        }

        Long id = null;
        String employeeNumber = null;
        String memo = null;
        ChannelType type = null;
        LocalDateTime at = null;
        String ipAddress = null;
        List<DiffDto> diffs = null;
        if ( entity != null ) {
            id = entity.getId();
            employeeNumber = entity.getEmployeeNumber();
            memo = entity.getMemo();
            type = entity.getType();
            at = entity.getCreatedAt();
            ipAddress = entity.getIpAddress();
            diffs = channelDiffListToDiffDtoList( entity.getDiffs() );
        }
        Long profileImageId = null;
        String employeeName = null;
        if ( employee != null ) {
            profileImageId = employeeProfileImageId( employee );
            employeeName = employee.getName();
        }

        ChangeLogDetailDto changeLogDetailDto = new ChangeLogDetailDto( id, type, employeeNumber, memo, ipAddress, at, employeeName, profileImageId, diffs );

        return changeLogDetailDto;
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

    protected DiffDto channelDiffToDiffDto(ChannelDiff channelDiff) {
        if ( channelDiff == null ) {
            return null;
        }

        String propertyName = null;
        String before = null;
        String after = null;

        propertyName = channelDiff.getPropertyName();
        before = channelDiff.getBefore();
        after = channelDiff.getAfter();

        DiffDto diffDto = new DiffDto( propertyName, before, after );

        return diffDto;
    }

    protected List<DiffDto> channelDiffListToDiffDtoList(List<ChannelDiff> list) {
        if ( list == null ) {
            return null;
        }

        List<DiffDto> list1 = new ArrayList<DiffDto>( list.size() );
        for ( ChannelDiff channelDiff : list ) {
            list1.add( channelDiffToDiffDto( channelDiff ) );
        }

        return list1;
    }
}
