package com.example.hrbank.domain.binarycontent.mapper;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-13T16:37:49+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.1.jar, environment: Java 17.0.17 (Amazon.com Inc.)"
)
@Component
public class BinaryContentMapperImpl implements BinaryContentMapper {

    @Override
    public BinaryContentDto toDto(BinaryContent entity) {
        if ( entity == null ) {
            return null;
        }

        BinaryContentDto.BinaryContentDtoBuilder binaryContentDto = BinaryContentDto.builder();

        binaryContentDto.id( entity.getId() );
        binaryContentDto.fileName( entity.getFileName() );
        binaryContentDto.contentType( entity.getContentType() );
        binaryContentDto.fileSize( entity.getFileSize() );
        binaryContentDto.createdAt( entity.getCreatedAt() );

        return binaryContentDto.build();
    }
}
