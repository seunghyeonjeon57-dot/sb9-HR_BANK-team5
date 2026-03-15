package com.example.hrbank.domain.backup.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBackupHistory is a Querydsl query type for BackupHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackupHistory extends EntityPathBase<BackupHistory> {

    private static final long serialVersionUID = -684921344L;

    public static final QBackupHistory backupHistory = new QBackupHistory("backupHistory");

    public final com.example.hrbank.global.entity.QBaseTimeEntity _super = new com.example.hrbank.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endedAt = createDateTime("endedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final EnumPath<BackupStatus> status = createEnum("status", BackupStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath worker = createString("worker");

    public QBackupHistory(String variable) {
        super(BackupHistory.class, forVariable(variable));
    }

    public QBackupHistory(Path<? extends BackupHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBackupHistory(PathMetadata metadata) {
        super(BackupHistory.class, metadata);
    }

}

