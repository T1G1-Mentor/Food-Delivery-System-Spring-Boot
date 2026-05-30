package com.mentorship.food_delivery_app.common.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.mentorship.food_delivery_app.cart.exceptions.CartLockedException;
import com.mentorship.food_delivery_app.cart.exceptions.ItemNotAvailableException;
import com.mentorship.food_delivery_app.cart.exceptions.RestaurantMismatchException;
import com.mentorship.food_delivery_app.common.dto.ErrorResponseDto;
import com.mentorship.food_delivery_app.common.dto.ValidationErrorResponse;
import com.mentorship.food_delivery_app.customer.exceptions.AddressNotFoundException;
import com.mentorship.food_delivery_app.customer.exceptions.CustomerNotFoundException;
import com.mentorship.food_delivery_app.customer.exceptions.PreferredPaymentWasNotConfiguredException;
import com.mentorship.food_delivery_app.order.exceptions.CancelledOrderException;
import com.mentorship.food_delivery_app.order.exceptions.DeliveredOrderException;
import com.mentorship.food_delivery_app.order.exceptions.OrderNotFoundException;
import com.mentorship.food_delivery_app.restaurant.exceptions.CouponNotFoundException;
import com.mentorship.food_delivery_app.restaurant.exceptions.ItemNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ValidationErrorResponse> buildValidationResponse(HttpStatus status, String message, Map<String, String> errors) {
        return ResponseEntity.status(status).
                body(
                        ValidationErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(status.value())
                                .error(status.getReasonPhrase())
                                .message(message)
                                .errors(errors)
                                .build()
                );
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    private Map<String, String> getStringStringMap(InvalidFormatException invalidFormatException) {
        Map<String, String> errors = new HashMap<>();

        String fieldName = invalidFormatException.getPath().getFirst().getFieldName();

        if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isEnum()) {
            String allowedValues = java.util.Arrays.toString(invalidFormatException.getTargetType().getEnumConstants());
            errors.put(fieldName, "Invalid value: '" + invalidFormatException.getValue() + "'. Allowed values are: " + allowedValues);
        } else {
            errors.put(fieldName, "Invalid data type provided.");
        }
        return errors;
    }
    // -------------------------------------------------------------------
    //  VALIDATION EXCEPTIONS,
    // -------------------------------------------------------------------

    /**
     * Handles validation errors for @Valid / @Validated on @RequestBody objects.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Validation failed on {} {} - Binding errors: {}",
                request.getMethod(), request.getRequestURI(), ex.getErrorCount());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );


        return buildValidationResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields",
                errors
        );

    }

    // -------------------------------------------------------------------
    //  SPRING WEB EXCEPTIONS (400, 404, 405)
    // -------------------------------------------------------------------

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException) {

            final Map<String, String> errors = getStringStringMap(invalidFormatException);

            return buildValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    "JSON parsing failed due to invalid data formatting",
                    errors
            );

        }

        log.warn("Malformed JSON payload on {} {}: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request body");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {

        log.warn("Missing required parameter '{}' on {} {}",
                ex.getParameterName(), request.getMethod(), request.getRequestURI());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Missing required parameter: " + ex.getParameterName());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoResourceFound(
            NoResourceFoundException ex, HttpServletRequest request) {

        log.warn("Resource not found: {} {} ", request.getMethod(), request.getRequestURI());

        return buildErrorResponse(HttpStatus.NOT_FOUND, "The requested endpoint does not exist");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        log.warn("Method not allowed. Attempted {} on {}. Supported methods: {}",
                request.getMethod(), request.getRequestURI(), ex.getSupportedHttpMethods());

        return buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not supported");
    }

    // -------------------------------------------------------------------
    // 3. DATABASE / DATA EXCEPTIONS (409 Conflict)
    // -------------------------------------------------------------------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        // CRITICAL SECURITY LOGGING: Print the full stack trace internally so you can see the exact SQL failure.
        log.error("CRITICAL: Data integrity violation on {} {}. Full details:",
                request.getMethod(), request.getRequestURI(), ex);

        // SANITIZED RESPONSE: The client gets zero information about the database schema.
        return buildErrorResponse(HttpStatus.CONFLICT,
                "The request could not be completed due to a data conflict (e.g., duplicate record)");
    }

    // -------------------------------------------------------------------
    // 4. CATCH-ALL UNEXPECTED EXCEPTIONS (500 Internal Server Error)
    // -------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllUnhandledExceptions(
            Exception ex, HttpServletRequest request) {

        // CRITICAL SECURITY LOGGING: Print the full stack trace internally for bug tracking.
        log.error("CRITICAL: Unhandled exception caught on {} {}. Full details:",
                request.getMethod(), request.getRequestURI(), ex);

        // SANITIZED RESPONSE: The client gets a generic message to prevent stack trace leaks.
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected internal server error occurred");
    }

    // -------------------------------------------------------------------
    // ADDITIONS: MEDIA TYPE, TYPE MISMATCH, AND AUTHORIZATION
    // -------------------------------------------------------------------

    /**
     * Handles unsupported content types (e.g., sending XML when JSON is required).
     */
    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupported(
            org.springframework.web.HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {

        log.warn("Unsupported media type '{}' on {} {}",
                ex.getContentType(), request.getMethod(), request.getRequestURI());

        return buildErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type. Please send the request body as " + ex.getSupportedMediaTypes());
    }

    /**
     * Handles type mismatches in URI parameters (e.g., passing 'abc' to a UUID field).
     */
    @ExceptionHandler(org.springframework.beans.TypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
            org.springframework.beans.TypeMismatchException ex, HttpServletRequest request) {

        log.warn("Type mismatch for property '{}' on {} {}",
                ex.getPropertyName(), request.getMethod(), request.getRequestURI());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid parameter type provided for: " + ex.getPropertyName());
    }

    /**
     * Handles method-level security authorization failures (@PreAuthorize).
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request) {

        // Security Log: Who tried to access what?
        log.warn("Access denied on {} {}. Reason: {}",
                request.getMethod(), request.getRequestURI(), ex.getMessage());

        return buildErrorResponse(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
    }


    // -------------------------------------------------------------------
    //  CUSTOM APPLICATION EXCEPTIONS
    // -------------------------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource Not found Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ResourceUnavailableException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceUnavailableException ex) {
        log.warn("Resource Unavailable Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(BadRequestException ex) {
        log.warn("Bad Request Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());

    }

    // -------------------------------------------------------------------
    //  CART EXCEPTIONS
    // -------------------------------------------------------------------

    @ExceptionHandler(CartLockedException.class)
    public ResponseEntity<ErrorResponseDto> handleCartLocked(CartLockedException ex) {
        log.warn("Cart Locked Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.CONFLICT, ex.getLocalizedMessage());
    }


    @ExceptionHandler(ItemNotAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleItemNotAvailable(ItemNotAvailableException ex) {
        log.warn("Item Not Available Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(RestaurantMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleRestaurantMismatch(RestaurantMismatchException ex) {
        log.warn("Restaurant Mismatch Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.CONFLICT, ex.getLocalizedMessage());
    }

    // -------------------------------------------------------------------
    //  RESTAURANT EXCEPTIONS
    // -------------------------------------------------------------------
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCouponNotFound(CouponNotFoundException ex) {
        log.warn("Coupon Not found Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRestaurantMismatch(ItemNotFoundException ex) {
        log.warn("Item Not Found Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    // -------------------------------------------------------------------
    //  CUSTOMER EXCEPTIONS
    // -------------------------------------------------------------------
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerNotFound(CustomerNotFoundException ex) {
        log.warn("Customer Not Found Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAddressNotFound(AddressNotFoundException ex) {
        log.warn("Address Not Found Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(PreferredPaymentWasNotConfiguredException.class)
    public ResponseEntity<ErrorResponseDto> handlePreferredPaymentWasNotConfigured(PreferredPaymentWasNotConfiguredException ex) {
        log.warn("Preferred Payment Was Not Configured Exception Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }
    // -------------------------------------------------------------------
    //  ORDER EXCEPTIONS
    // -------------------------------------------------------------------
    @ExceptionHandler(CancelledOrderException.class)
    public ResponseEntity<ErrorResponseDto> handleCancelledOrder(CancelledOrderException ex) {
        log.warn("Cancelled Order Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(DeliveredOrderException.class)
    public ResponseEntity<ErrorResponseDto> handleDeliveredOrder(DeliveredOrderException ex) {
        log.warn("Delivered Order Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleOrderNotFound(OrderNotFoundException ex) {
        log.warn("Order Not Found Exception was thrown with cause: {}", ex.getLocalizedMessage());

        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }
}
