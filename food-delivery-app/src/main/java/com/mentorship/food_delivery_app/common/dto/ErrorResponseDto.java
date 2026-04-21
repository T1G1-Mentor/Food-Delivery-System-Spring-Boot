package com.mentorship.food_delivery_app.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDto{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd - HH:mm:ss")
    private final LocalDateTime timestamp;

    private final Integer status;

    private final String error;

    private final String message;

}
