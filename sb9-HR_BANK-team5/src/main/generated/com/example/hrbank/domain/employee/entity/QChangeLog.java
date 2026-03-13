package com.example.hrbank.domain.employee.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChangeLog is a Querydsl query type for ChangeLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChangeLog extends EntityPathBase<ChangeLog> {

    private static final long serialVersionUID = -2100602026L;

    public static final QChangeLog changeLog = new QChangeLog("changeLog");

    public final DateTimePath<java.time.LocalDateTime> at = createDateTime("at", java.time.LocalDateTime.class);

    public final ListPath<ChannelDiff, QChannelDiff> diffs = this.<ChannelDiff, QChannelDiff>createList("diffs", ChannelDiff.class, QChannelDiff.class, PathInits.DIRECT2);

    public final StringPath employeeNumber = createString("employeeNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ipAddress = createString("ipAddress");

    public final StringPath memo = createString("memo");

    public final EnumPath<com.example.hrbank.domain.employee.entity.enums.ChannelType> type = createEnum("type", com.example.hrbank.domain.employee.entity.enums.ChannelType.class);

    public QChangeLog(String variable) {
        super(ChangeLog.class, forVariable(variable));
    }

    public QChangeLog(Path<? extends ChangeLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChangeLog(PathMetadata metadata) {
        super(ChangeLog.class, metadata);
    }

}

