package jude.carrot.apiserver.user.controller;

import jakarta.validation.Valid;
import jude.carrot.apiserver.common.dto.response.ApiResponse;
import jude.carrot.apiserver.common.status.Status;
import jude.carrot.apiserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static jude.carrot.apiserver.user.dto.request.UserRequest.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return ResponseEntity.ok(ApiResponse.of(Status.SUCCESS));
    }
}
