package com.mentorship.food_delivery_app.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
public class ValidationErrorResponse {
    Map<String, String> errors;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd - HH:mm:ss")
    private LocalDateTime timestamp;
    private Integer status;
    /**
     * {@summary Short description of the error type}
     *
     */
    private String error;
    /**
     * {@summary Detailed error message}
     *
     */
    private String message;

}
