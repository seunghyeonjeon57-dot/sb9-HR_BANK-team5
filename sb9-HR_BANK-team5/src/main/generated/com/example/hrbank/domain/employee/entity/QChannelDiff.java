package com.example.hrbank.domain.employee.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChannelDiff is a Querydsl query type for ChannelDiff
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChannelDiff extends EntityPathBase<ChannelDiff> {

    private static final long serialVersionUID = 1901917450L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChannelDiff channelDiff = new QChannelDiff("channelDiff");

    public final StringPath after = createString("after");

    public final StringPath before = createString("before");

    public final QChangeLog changeLog;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath propertyName = createString("propertyName");

    public QChannelDiff(String variable) {
        this(ChannelDiff.class, forVariable(variable), INITS);
    }

    public QChannelDiff(Path<? extends ChannelDiff> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChannelDiff(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChannelDiff(PathMetadata metadata, PathInits inits) {
        this(ChannelDiff.class, metadata, inits);
    }

    public QChannelDiff(Class<? extends ChannelDiff> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.changeLog = inits.isInitialized("changeLog") ? new QChangeLog(forProperty("changeLog")) : null;
    }

}

