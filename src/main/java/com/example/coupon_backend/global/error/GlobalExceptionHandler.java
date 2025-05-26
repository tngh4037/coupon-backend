package com.example.coupon_backend.global.error;

import com.example.coupon_backend.global.error.code.CommonErrorCode;
import com.example.coupon_backend.global.error.code.ErrorCode;
import com.example.coupon_backend.global.error.exception.BusinessException;

import com.example.coupon_backend.global.util.MessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageHelper messageHelper;

    /**
     *  @RequestBody @Valid/@Validated binding/validation error ( JSON 바디 -> DTO 바인딩 )
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("handleMethodArgumentNotValidException", e);

        List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, fieldErrors);

        return new ResponseEntity<>(response, CommonErrorCode.INVALID_INPUT_VALUE.getHttpStatus());
    }

    /**
     * @ModelAttribute @Valid binding/validation error ( 쿼리 파라미터 or Form -> DTO 바인딩 )
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.info("handleBindException", e);

        List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, fieldErrors);

        return new ResponseEntity<>(response, CommonErrorCode.INVALID_INPUT_VALUE.getHttpStatus());
    }

    /**
     * @RequestParam, @PathVariable, @PathParam 등 typeMismatch error ( 단일 파라미터 타입 불일치 )
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("handleMethodArgumentTypeMismatchException", e);
        String value = e.getValue() == null ? "" : e.getValue().toString();
        String field = e.getName();
        String message = messageHelper.getMessage(e.getErrorCode());

        ErrorResponse.FieldError error = new ErrorResponse.FieldError(field, value, message);
        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_TYPE_VALUE, List.of(error));

        return new ResponseEntity<>(response, CommonErrorCode.INVALID_TYPE_VALUE.getHttpStatus());
    }

    /**
     * unsupported HTTP method
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(CommonErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, CommonErrorCode.METHOD_NOT_ALLOWED.getHttpStatus());
    }

    /**
     * application predictable exception
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.info("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * application unPredictable exception
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, CommonErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    private List<ErrorResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.getField(),
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                        messageHelper.getBindingErrorMessage(error)))
                .collect(Collectors.toList());
    }
}
