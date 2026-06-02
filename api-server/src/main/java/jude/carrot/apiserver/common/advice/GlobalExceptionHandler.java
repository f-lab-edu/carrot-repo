package jude.carrot.apiserver.common.advice;

import jude.carrot.apiserver.common.dto.response.ApiResponse;
import jude.carrot.apiserver.common.exception.CustomException;
import jude.carrot.apiserver.common.status.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.of(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse(Status.INVALID_REQUEST.getDetailMessage());
        return ResponseEntity.badRequest().body(
                ApiResponse.<Void>builder()
                        .code(Status.INVALID_REQUEST.getCode())
                        .detailMessage(message)
                        .httpStatus(Status.INVALID_REQUEST.getHttpStatus())
                        .build()
        );
    }
}
