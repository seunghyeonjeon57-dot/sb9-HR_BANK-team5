package com.example.hrbank.domain.backup.mapper;

import com.example.hrbank.domain.backup.dto.response.BackupCursorPageResponse;
import com.example.hrbank.domain.backup.dto.response.BackupResponse;
import com.example.hrbank.domain.backup.entity.BackupHistory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-16T09:41:52+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 17.0.17 (Amazon.com Inc.)"
)
@Component
public class BackupMapperImpl implements BackupMapper {

    @Override
    public BackupResponse toResponse(BackupHistory entity) {
        if ( entity == null ) {
            return null;
        }

        BackupResponse.BackupResponseBuilder backupResponse = BackupResponse.builder();

        backupResponse.id( entity.getId() );
        backupResponse.worker( entity.getWorker() );
        backupResponse.startedAt( entity.getStartedAt() );
        backupResponse.endedAt( entity.getEndedAt() );
        backupResponse.status( entity.getStatus() );
        backupResponse.fileId( entity.getFileId() );

        return backupResponse.build();
    }

    @Override
    public List<BackupResponse> toResponseList(List<BackupHistory> entities) {
        if ( entities == null ) {
            return null;
        }

        List<BackupResponse> list = new ArrayList<BackupResponse>( entities.size() );
        for ( BackupHistory backupHistory : entities ) {
            list.add( toResponse( backupHistory ) );
        }

        return list;
    }

    @Override
    public BackupCursorPageResponse toPageResponse(List<BackupHistory> entities, String nextCursor, Long nextIdAfter, int size, long totalElements, boolean hasNext) {
        if ( entities == null && nextCursor == null && nextIdAfter == null ) {
            return null;
        }

        BackupCursorPageResponse.BackupCursorPageResponseBuilder backupCursorPageResponse = BackupCursorPageResponse.builder();

        backupCursorPageResponse.content( toResponseList( entities ) );
        backupCursorPageResponse.nextCursor( nextCursor );
        backupCursorPageResponse.nextIdAfter( nextIdAfter );
        backupCursorPageResponse.size( size );
        backupCursorPageResponse.totalElements( totalElements );
        backupCursorPageResponse.hasNext( hasNext );

        return backupCursorPageResponse.build();
    }
}
