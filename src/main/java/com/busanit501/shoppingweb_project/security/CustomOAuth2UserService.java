package com.busanit501.shoppingweb_project.security;

import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;
        if ("kakao".equals(clientName)) {
            email = getKakaoEmail(paramMap);
        }

        return generateDTO(email, paramMap);
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params) {
        Optional<Member> result = memberRepository.findByEmail(email);

        // 닉네임(이름) 꺼내기
        String nickname = null;
        if (params.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) params.get("kakao_account");
            if (kakaoAccount.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                nickname = (String) profile.get("nickname");
            }
        }
        if (result.isEmpty()) {
            Member member = Member.builder()
                    .memberId(email)
                    .password(passwordEncoder.encode("SOCIAL_LOGIN"))
                    .email(email)
                    .userName(nickname)
                    .role("ROLE_USER")
                    .build();

            memberRepository.save(member);
        }

        Member member = memberRepository.findByEmail(email).get();

        MemberSecurityDTO dto = new MemberSecurityDTO(
                member.getMemberId(),
                member.getPassword(),
                member.getEmail(),
                false,
                true,
                List.of(new SimpleGrantedAuthority(member.getRole()))
        );

        dto.setProps(params);
        return dto;
    }

    private String getKakaoEmail(Map<String, Object> params) {
        Map<String, Object> accountMap = (Map<String, Object>) params.get("kakao_account");
        return (String) accountMap.get("email");
    }
}
