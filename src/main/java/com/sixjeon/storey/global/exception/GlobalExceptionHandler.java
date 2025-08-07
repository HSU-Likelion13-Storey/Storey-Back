package com.sixjeon.storey.global.exception;



import com.sixjeon.storey.global.response.ErrorResponse;
import com.sixjeon.storey.global.response.code.ErrorResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // @RequestBody로 받은 객체에 @Valid(or @Validated)를 적용했을 때
    // 필드에 선언된 검증 조건 위반 시 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.of(
                ErrorResponseCode.INVALID_HTTP_MESSAGE_BODY, e.getFieldError().getDefaultMessage());
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    // @RequestParam, @ModelAttribute로 받은 객체에 @Valid(or @Validated)를 적용했을 때
    // 선언된 검증 조건 위반 시 발생
    @ExceptionHandler(BindException.class)
    private ResponseEntity<ErrorResponse<?>> handleBindException(BindException e) {
        log.error("BindException : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.of(
                ErrorResponseCode.INVALID_HTTP_MESSAGE_BODY, e.getFieldError().getDefaultMessage());
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    // RequestBody 등으로 전달받은 JSON 바디의 파싱이 실패했을 때(JSON 문법 오류, 타입 불일치, 필수 바디 없음... 등등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.from(
                ErrorResponseCode.INVALID_HTTP_MESSAGE_BODY);
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    // 요청 파라미터가 타입 변환에 실패했을 때 (enum type 불일치, 쿼리/경로 파라미터의 타입 변환이 실패)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.from(
                ErrorResponseCode.INVALID_HTTP_MESSAGE_PARAMETER
        );
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    // Request Part 누락 시 발생
    @ExceptionHandler(MissingServletRequestPartException.class)
    private ResponseEntity<ErrorResponse<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        log.error("MissingServletRequestPartException : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.from(
                ErrorResponseCode.INVALID_HTTP_MESSAGE_PARAMETER
        );
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    // 지원하지 않는 HTTP 메소드를 호출할 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ErrorResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.from(
                ErrorResponseCode.UNSUPPORTED_HTTP_METHOD
        );
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    // 존재하지 않는 엔드포인트 호출 시 발생
    @ExceptionHandler(NoHandlerFoundException.class)
    private ResponseEntity<ErrorResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.from(
                ErrorResponseCode.NOT_FOUND_ENDPOINT
        );
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }


    // 비즈니스 로직 에러
    @ExceptionHandler(BaseException.class)
    private ResponseEntity<ErrorResponse<?>> handleBaseException(BaseException e) {
        log.error("BaseException : {}", e.getBaseResponseCode().getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.from(
                e.getBaseResponseCode()
        );
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);
    }

    // 나머지 예외 처리
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse<?>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        ErrorResponse<?> errorResponse = ErrorResponse.from(
                ErrorResponseCode.SERVER_ERROR
        );
        return ResponseEntity.status(errorResponse.getHttpStatus()).body(errorResponse);

    }
}
