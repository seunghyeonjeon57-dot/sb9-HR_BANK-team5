package com.example.hrbank.domain.binarycontent.mapper;

import com.example.hrbank.domain.binarycontent.dto.data.BinaryContentDto;
import com.example.hrbank.domain.binarycontent.entity.BinaryContent;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BinaryContentMapper {
  BinaryContentDto toDto(BinaryContent entity);
}