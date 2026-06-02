package jude.carrot.apiserver.common.dto.response;

import jude.carrot.apiserver.common.exception.CustomException;
import jude.carrot.apiserver.common.status.Status;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponse<T> {
    private String code;
    private String detailMessage;
    private HttpStatus httpStatus;
    private T data;

    public static <T> ApiResponse<T> of(T data, Status status){
        return  ApiResponse.<T>builder()
                .data(data)
                .code(status.getCode())
                .detailMessage(status.getDetailMessage())
                .httpStatus(status.getHttpStatus())
                .build();
    }

    public static ApiResponse<Void> of(Status status){
        return  ApiResponse.<Void>builder()
                .code(status.getCode())
                .detailMessage(status.getDetailMessage())
                .httpStatus(status.getHttpStatus())
                .build();
    }
    public static ApiResponse<Void> of(CustomException e){
        return ApiResponse.<Void>builder()
                .code(e.getCode())
                .detailMessage(e.getDetailMessage())
                .httpStatus(e.getHttpStatus())
                .build();
    }
}
