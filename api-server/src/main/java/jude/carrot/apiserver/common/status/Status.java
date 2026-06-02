package jude.carrot.apiserver.common.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Status {

    SUCCESS("SC", "success", HttpStatus.OK),
    USER_EXIST("UE", "user already exists", HttpStatus.CONFLICT),
    INVALID_REQUEST("IR", "invalid request", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("ISE", "internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String detailMessage;
    private final HttpStatus httpStatus;
}
