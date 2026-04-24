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
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 참고) 요청 데이터 관련 대표적인 예외 4가지
 * : MethodArgumentNotValidException, BindException
 * : MissingServletRequestParameterException, MethodArgumentTypeMismatchException
 *
 * 1) 객체 바인딩 관련 예외 ( 객체로 파라미터를 받을 때 발생, 스프링이 내부적으로 바인딩 및 검증을 시도하다 실패하면 발생함 )
 * - 1-1) "@RequestBody + @Valid", JSON 바인딩 실패 (유효성 검증)
 *  ㄴ MethodArgumentNotValidException
 * - 1-2) "@ModelAttribute + @Valid or @Validated", 폼/쿼리 파라미터 바인딩 실패 또는 유효성 실패
 *  ㄴ BindException
 *
 * 2) 단일 값 관련 예외
 * - 2-1) "@RequestParam(required = true)" 인데 파라미터가 아예 누락된 경우
 *  ㄴ MissingServletRequestParameterException
 * - 2-2) "@RequestParam, @PathVariable" 의 타입 변환 실패 시 발생
 *  ㄴ MethodArgumentTypeMismatchException
 *
 * 참고) @PathVariable이 필수인데 누락되면 404 (예외 발생은 안 함)
 * - 타입 변환 실패 시에는 MethodArgumentTypeMismatchException 발생
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageHelper messageHelper;

    /**
     * 필수 파라미터가 누락되었을 때
     *
     * - 컨트롤러 정의: @GetMapping("/search") public String search(@RequestParam(required=true) String keyword) {..}
     * - 호출: GET /search
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameter Exception occurred. parameterName={}, message={}, className={}", 
                e.getParameterName(), e.getMessage(), e.getClass().getName());

        final ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE);

        return ResponseEntity
                .status(CommonErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(response);
    }

    /**
     * 요청 파라미터의 타입이 메서드 파라미터와 호환되지 않을 때
     *
     * - 컨트롤러 정의: @GetMapping("/users/{id}") public String getUser(@PathVariable Long id) {..}
     * - 요청: GET /users/abc ( 숫자 X )
     *
     * - 참고) @RequestParam, @PathVariable, @PathParam 등 typeMismatch error ( 단일 파라미터 타입 불일치 )
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
     * @RequestBody 에서 @Valid 또는 @Validated를 사용해 요청 객체를 검증할 때, 유효성 검사를 통과하지 못하면 발생
     * - e.g. NotBlank 위반 ( 빈 문자열 ), Min(0) 위반 등 ( 음수 값 )
     *
     * - 참고) @RequestBody @Valid/@Validated binding/validation error ( JSON 바디 -> DTO 바인딩 )
     * - 참고) JSON -> 객체 바인딩 후 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("handleMethodArgumentNotValidException", e);

        List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, fieldErrors);

        // 참고) ResponseEntity 반환: 내부에서 sendError(400)를 호출하지 X, HttpServletResponse.setStatus(400) 만 적용 -> WAS의 에러 처리 로직을 우회하고 직접 응답 구성
        return new ResponseEntity<>(response, CommonErrorCode.INVALID_INPUT_VALUE.getHttpStatus());
    }

    /**
     * @ModelAttribute 에시 바인딩 시 유효성 검사 실패 ( 주로 Spring MVC 폼 방식에서 발생 )
     *
     * - 참고) @ModelAttribute @Valid binding/validation error ( 쿼리 파라미터 or Form -> DTO 바인딩 )
     * - 참고) 쿼리/form -> 객체 바인딩 or 검증 실패
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.info("handleBindException", e);

        List<ErrorResponse.FieldError> fieldErrors = getFieldErrors(e.getBindingResult());
        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, fieldErrors);

        return new ResponseEntity<>(response, CommonErrorCode.INVALID_INPUT_VALUE.getHttpStatus());
    }

    /**
     * NoResourceFoundException
     * - spring boot version 3.2 이후 부터, 존재하지 않는 리소스 요청시 sendError(404) 처리하는 것이 아닌, NoResourceFoundException 예외가 던져진다.
     * - 별도로 설정이 없으면 이 예외는 스프링 내부 예외 처리 매커니즘에 의해 핸들링되어, 404 로 내려지는 것 같은데, 만약 개발자가 예외처리 핸들러에서 Exception 으로 잡아버리면, 여기에 우선 적용되어버려, 결과적으로 500 오류가 반환된다.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.info("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(CommonErrorCode.NOT_FOUND);
        return new ResponseEntity<>(response, CommonErrorCode.NOT_FOUND.getHttpStatus());
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
