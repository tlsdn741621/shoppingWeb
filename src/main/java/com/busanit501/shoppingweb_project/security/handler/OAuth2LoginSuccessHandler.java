package com.busanit501.shoppingweb_project.security.handler;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import com.busanit501.shoppingweb_project.security.MemberSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("OAuth2 로그인 성공 핸들러 진입");
        Object principal = authentication.getPrincipal();
        log.info("principal 클래스: {}", principal.getClass().getName());

        if (!(principal instanceof MemberSecurityDTO)) {
            log.error("principal이 MemberSecurityDTO가 아님. principal: {}", principal);
            response.sendRedirect("/login?error=invalid_user");
            return;
        }

        MemberSecurityDTO userDetails = (MemberSecurityDTO) principal;

        memberRepository.findByMemberId(userDetails.getMemberId())
                .ifPresentOrElse(member -> {
                    try {
                        boolean needsPhone = member.getPhone() == null || member.getPhone().isBlank();
                        boolean needsAddress = member.getAddresses() == null || member.getAddresses().stream().noneMatch(Address::isDefault);

                        if (needsPhone || needsAddress) {
                            log.info("프로필 미완성, /complete-profile 리다이렉트");
                            response.sendRedirect("/complete-profile");
                        } else {
                            log.info("프로필 완성, /home 리다이렉트");
                            response.sendRedirect("/home");
                        }
                    } catch (IOException e) {
                        log.error("리다이렉트 실패", e);
                    }
                }, () -> {
                    log.error("해당 회원 없음: {}", userDetails.getMid());
                    try {
                        response.sendRedirect("/login?error=user_not_found");
                    } catch (IOException e) {
                        log.error("리다이렉트 실패", e);
                    }
                });
    }

}
