package com.example.hrbank.domain.binarycontent.dto.request;

public record BinaryContentRequest(
    String fileName,
    String contentType,
    Long fileSize)
{ }
