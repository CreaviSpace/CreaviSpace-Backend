package com.creavispace.project.config;

import static org.junit.jupiter.api.Assertions.*;

import com.creavispace.project.config.auth.SecurityConfig;
import jakarta.servlet.http.HttpSession;
import net.minidev.json.writer.BeansMapper.Bean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginControllerTest {

    @Autowired
    HttpSession session;
    @Autowired
    private LoginController loginController;

//    @Test
//    void accessToken() {
//        String token = loginController.accessToken("GoUAVvkbBhpiBcqmCS", "4FKY8SHJ8qm4zl2-Yo7YY0P9Dgb9nufNs40RYXWA3Fw")
//                .getBody();
//        loginController.getJwt(token);
//    }
}