package com.creavispace.project.config;

import com.creavispace.project.config.auth.dto.JwtDto;
import com.creavispace.project.config.auth.utils.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Component
public class LoginController {

    @GetMapping("/login")
    public ResponseEntity<JwtDto> login(
            @RequestParam("token") String tokenName) {
        System.out.println("LoginController.login");
        JwtDto token = JwtManager.findToken(tokenName);

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
