package com.creavispace.project.config;

import com.creavispace.project.config.auth.dto.JsonToken;
import com.creavispace.project.config.auth.utils.AccessTokenJsonParser;
import com.creavispace.project.config.auth.dto.NaverLoginData;
import com.creavispace.project.config.auth.utils.JwtUtil;
import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.member.service.MemberService;
import io.swagger.v3.core.util.Json;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
@Component
public class LoginController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private final HttpSession httpSession;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${jwt.secret}")
    private String jwtSecret;
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    private List<String> testList = List.of("1", "2", "3", "4", "5");
    private static final String token_baseUrl = "https://nid.naver.com/oauth2.0/token?";
    private static final String grantAuthorize = "authorization_code";
    private static final String grantRefresh = "refresh_token";
    private static final String grantDelete = "delete";

    @GetMapping("/config/login")
    public ResponseEntity<JsonToken> getJwt(Member member) {
        String jwt = JwtUtil.createJwt(member.getMemberEmail(), member.getLoginType(), member.getId(), jwtSecret,
                1000 * 60 * 60L);
        System.out.println("json Token= " + jwt);
        sessionStore.put("jwt", "Bearer " + jwt);

        JsonToken jsonToken = new JsonToken(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(jsonToken);
    }

    @GetMapping("/login/get/store")
    public ResponseEntity<String> getStore(HttpServletResponse response) throws IOException {
        String store = sessionStore.get("accessToken").toString();
        System.out.println("store = " + store);
        return ResponseEntity.status(HttpStatus.OK).body(store);
    }

    @GetMapping("/login/authorize")
    public void getAuthorize() {
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        //https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=jyvqXeaVOVmV&redirect_uri=http%3A%2F%2Fservice.redirect.url%2Fredirect&state=hLiDdL2uhPtsftcU
        httpSession.setAttribute("state", state);
        RestTemplate restTemplate = new RestTemplate();
        // API 호출
        ResponseEntity<String> response = restTemplate.getForEntity(
                "https://nid.naver.com/oauth2.0/authorize?"
                        + "response_type=" + "code"
                        + "&client_id=" + naverClientId
                        + "&redirect_uri=" + "https://creavispace.vercel.app"
                        + "&state=" + state
                , String.class);

        System.out.println(response);
    }

    @GetMapping("login/get/code")
    public void getCode() {
        String url = "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=KsvXIMg4NMC9CRgZ2wvg"
                + "&state=hLiDdL2uhPtsftcU"
                + "&redirect_uri=https://creavispace.vercel.app/login";


    }

    @GetMapping("/login/naver")
    public ResponseEntity<AccessTokenJsonParser> naverAccessToken(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam String code,
            @RequestParam String state) throws IOException {
        System.out.println(httpServletRequest.getContextPath());
        RestTemplate restTemplate = new RestTemplate();
        // API 호출
        ResponseEntity<AccessTokenJsonParser> response = restTemplate.getForEntity(
                token_baseUrl
                        + "grant_type=" + grantAuthorize
                        + "&client_secret=" + naverClientSecret
                        + "&client_id=" + naverClientId
                        + "&code=" + code
                        + "&state=" + state
                , AccessTokenJsonParser.class);
        if (response.getBody().getAccessToken() == null) {
            response.getBody().setMessage("토큰 획득 실패");
        }
        System.out.println("발급받은 액세스 토큰 = " + response);
        NaverLoginData naverLoginData = userInfoForNaver(response);
        Optional<Member> member = memberService.findByLoginId(naverLoginData.getNaverUserResponseData().getLoginId());
        if (member.isPresent()) {
            System.out.println("LoginController.naverAccessToken : jwt 발급, 헤더에 추가");
            httpServletResponse.addHeader(HttpHeaders.AUTHORIZATION,  getJwt(member.get()).getBody().getToken());

            String jwt = httpServletResponse.getHeader(HttpHeaders.AUTHORIZATION);
            System.out.println("Header jwt = " + jwt);
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            httpServletResponse.addCookie(cookie);
        }
        httpServletResponse.sendRedirect("https://creavispace.vercel.app/");
        System.out.println("LoginController.naverAccessToken : 페이지 전환");

        return response;
    }

    private NaverLoginData userInfoForNaver(ResponseEntity<AccessTokenJsonParser> response) {
        String accessToken = response.getBody().getAccessToken();
        if (response.getBody().getAccessToken() == null) {
            return null;
        }
        // 응답 처리
        System.out.println("LoginController.userInfoForNaver");
        if (sessionStore.get("refreshToken") == null) {
            sessionStore.put("refreshToken", response.getBody().getRefreshToken());
            sessionStore.put("accessToken", response.getBody().getAccessToken());
        }

        NaverLoginData userData = getUserData(accessToken);
        return userData;
    }

    @GetMapping("/login/naver/token/delete")
    public void naverDeleteToken() {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = sessionStore.get("accessToken").toString();

        ResponseEntity<AccessTokenJsonParser> response = restTemplate.getForEntity(
                token_baseUrl
                        + "grant_type=delete"
                        + "&client_secret=" + naverClientSecret
                        + "&client_id=" + naverClientId
                        + "&access_token=" + sessionStore.get("accessToken")
                        + "&service_provider=Naver"
                , AccessTokenJsonParser.class);
        System.out.println("삭제된 토큰 = " + response);
    }

    @GetMapping("/login/naver/refresh_token")
    public ResponseEntity<AccessTokenJsonParser> refreshToken() {
        RestTemplate restTemplate = new RestTemplate();
        String refreshToken = sessionStore.getOrDefault("refreshToken", null).toString();
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AccessTokenJsonParser(/*new IllegalAccessException()*/));
        }
        ResponseEntity<AccessTokenJsonParser> response;
        try {
            response = restTemplate.getForEntity(
                    token_baseUrl
                            + "grant_type=refresh_token"
                            + "&client_id=" + naverClientId
                            + "&client_secret=" + naverClientSecret
                            + "&refresh_token=" + refreshToken
                    , AccessTokenJsonParser.class);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AccessTokenJsonParser(/*new IllegalAccessException()*/));
        }
        System.out.println("새로 발급받은 토큰 = " + response);
        return response;
    }

    private NaverLoginData getUserData(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        ResponseEntity<NaverLoginData> response;
        try {
            response = restTemplate.exchange(
                    "https://openapi.naver.com/v1/nid/me",
                    HttpMethod.GET,
                    new HttpEntity<>(httpHeaders),
                    NaverLoginData.class
            );

        } catch (HttpClientErrorException e) {
            //만약 토큰이 만료 된 경우
            String refreshedToken = refreshToken().getBody().getAccessToken();
            if (refreshedToken == null) {
                System.out.println("LoginController.getUserData" + ": 사용자 정보를 가져오지 못했습니다.");
                return new NaverLoginData();
            }
            return getUserData(refreshedToken);
        }
        System.out.println("네이버 로그인 정보 = " + response);
        return response.getBody();
    }
}
