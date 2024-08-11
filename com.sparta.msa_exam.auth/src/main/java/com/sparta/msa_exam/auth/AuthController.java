package com.sparta.msa_exam.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/auth/signIn")
    public ResponseEntity<?> createAuthToken(@RequestParam String user_id) {
        // JWT 토큰 생성
        String token = authService.createAccessToken(user_id);

        // Authorization 헤더에 Bearer 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add("Server-Port", String.valueOf(serverPort));

        // 응답 헤더와 함께 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(new AuthResponse(token));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthResponse {
        private String access_token;
    }

}
