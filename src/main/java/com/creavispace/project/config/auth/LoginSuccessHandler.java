package com.creavispace.project.config.auth;

import com.creavispace.project.domain.member.entity.Member;
import com.creavispace.project.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private final MemberService memberService;

    private final HttpSession httpSession;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("LoginSuccessHandler.onAuthenticationSuccess");
        Optional<Member> loginId = memberService.findByLoginId(authentication.getName());
        if (loginId.isPresent()) {
            log.info("헤더에 jwt 추가");
            Member member = loginId.get();
            String jwt = memberService.login(member.getMemberEmail(), member.getLoginType(), member.getId());
            response.setHeader(HttpHeaders.AUTHORIZATION, jwt);
            httpSession.setAttribute("jwt", jwt);
            String header = response.getHeader(HttpHeaders.AUTHORIZATION);
            System.out.println("header = " + header);
            response.addCookie(new Cookie("cdcdcd", "cdcdcd"));
        }
        System.out.println("authentication = " + authentication.getName());
        System.out.println("authentication = " + authentication.getAuthorities().stream().collect(Collectors.toList()));
        HttpSession session = request.getSession();

//        getRedirectStrategy().sendRedirect(request, response, "https://creavispace.vercel.app?access_token=" + session.getAttribute("accessToken"));
        getRedirectStrategy().sendRedirect(request, response,
                "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=KsvXIMg4NMC9CRgZ2wvg"
                + "&state=hLiDdL2uhPtsftcU"
                + "&redirect_uri=http://localhost:8080/login/naver");
        //302는 임시 리디렉션으로 클라이언트가 재요청을 보내야함
    }
}
