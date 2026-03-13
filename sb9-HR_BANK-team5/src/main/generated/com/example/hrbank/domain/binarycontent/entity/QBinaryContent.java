package com.example.hrbank.domain.binarycontent.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBinaryContent is a Querydsl query type for BinaryContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBinaryContent extends EntityPathBase<BinaryContent> {

    private static final long serialVersionUID = -1969437514L;

    public static final QBinaryContent binaryContent = new QBinaryContent("binaryContent");

    public final com.example.hrbank.global.entity.QBaseTimeEntity _super = new com.example.hrbank.global.entity.QBaseTimeEntity(this);

    public final StringPath contentType = createString("contentType");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBinaryContent(String variable) {
        super(BinaryContent.class, forVariable(variable));
    }

    public QBinaryContent(Path<? extends BinaryContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBinaryContent(PathMetadata metadata) {
        super(BinaryContent.class, metadata);
    }

}

