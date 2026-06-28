package cn.bugstack.trigger.config;

import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.types.common.ResponseCode;
import cn.bugstack.types.exception.AppException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import cn.bugstack.types.common.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Global Exception Handler
 * <p>
 * Unified exception handling for all REST controllers.
 * Handles business exceptions, validation errors, and system exceptions.
 * </p>
 *
 * @author bugstack.cn
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Log log = Log.get(GlobalExceptionHandler.class);

    /**
     * Handle business exceptions (AppException)
     */
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response<Void> handleAppException(AppException e) {
        log.warn("Business exception occurred: code={}, info={}", e.getCode(), e.getInfo());
        return Response.<Void>builder()
                .code(e.getCode())
                .info(e.getInfo())
                .build();
    }

    /**
     * Handle request body validation errors (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Request body validation failed: {}", message);
        return Response.<Void>builder()
                .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                .info(message)
                .build();
    }

    /**
     * Handle bind exceptions (BindException)
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Bind validation failed: {}", message);
        return Response.<Void>builder()
                .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                .info(message)
                .build();
    }

    /**
     * Handle constraint violation exceptions (ConstraintViolationException)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.warn("Constraint validation failed: {}", message);
        return Response.<Void>builder()
                .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                .info(message)
                .build();
    }

    /**
     * Handle missing request parameter (MissingServletRequestParameterException)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = "Missing required parameter: " + e.getParameterName();
        log.warn("Missing request parameter: {}", e.getParameterName());
        return Response.<Void>builder()
                .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                .info(message)
                .build();
    }

    /**
     * Handle argument type mismatch (MethodArgumentTypeMismatchException)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = "Parameter '" + e.getName() + "' type mismatch, expected: " + e.getRequiredType();
        log.warn("Argument type mismatch: name={}, requiredType={}", e.getName(), e.getRequiredType());
        return Response.<Void>builder()
                .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                .info(message)
                .build();
    }

    /**
     * Handle unreadable HTTP message (HttpMessageNotReadableException)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Failed to read HTTP message: {}", e.getMessage());
        return Response.<Void>builder()
                .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                .info("Request body format error")
                .build();
    }

    /**
     * Handle illegal argument (IllegalArgumentException)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return Response.<Void>builder()
                .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                .info(e.getMessage())
                .build();
    }

    /**
     * Handle all other uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Void> handleException(Exception e) {
        log.error("Unexpected exception occurred", e);
        return Response.<Void>builder()
                .code(ResponseCode.UN_ERROR.getCode())
                .info(ResponseCode.UN_ERROR.getInfo())
                .build();
    }
}