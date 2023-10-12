package com.example.ticket.exception;

import lombok.Getter;

@Getter
public class DataResponseDTO<T> extends ResponseDTO {

    private final T data;

    private DataResponseDTO(T data) {
        super(true, Code.OK.getCode(), Code.OK.getMessage());
        this.data = data;
    }

    private DataResponseDTO(T data, String message) {
        super(true, Code.OK.getCode(), message);
        this.data = data;
    }

    public static <T> DataResponseDTO<T> of(T data) {
        return new DataResponseDTO<>(data);
    }

    public static <T> DataResponseDTO<T> of(T data, String message) {
        return new DataResponseDTO<>(data, message);
    }

    public static <T> DataResponseDTO<T> empty() {
        return new DataResponseDTO<>(null);
    }
}
