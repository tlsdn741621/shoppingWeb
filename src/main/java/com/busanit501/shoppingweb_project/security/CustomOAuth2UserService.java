package com.busanit501.shoppingweb_project.security;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.repository.AddressRepository;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.*;

@Log4j2
@Service
@Component
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

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
        if (email == null || email.isBlank()) {
            log.error("이메일이 null 또는 빈 값입니다. 회원 생성 불가");
            throw new IllegalArgumentException("이메일이 없어서 회원가입 불가");
        }

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
                    .social(true)
                    .build();

            memberRepository.save(member);
            log.info("회원 저장 완료: {}", member);
        }

        memberRepository.flush();


        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("회원이 등록됐으나 조회 불가"));

        boolean hasDefaultAddress = member.getAddresses().stream()
                .anyMatch(Address::isDefault);

        MemberSecurityDTO dto = new MemberSecurityDTO(
                member.getId(),
                member.getMemberId(),
                member.getPassword(),
                member.getEmail(),
                false,
                true,
                List.of(new SimpleGrantedAuthority(member.getRole()))
        );

        dto.setPhone(member.getPhone()); // <- 기존에 전화번호 저장했던 경우
        dto.setDefaultAddressExists(hasDefaultAddress);
        dto.setProps(params);
        return dto;
    }

    private String getKakaoEmail(Map<String, Object> params) {
        Map<String, Object> accountMap = (Map<String, Object>) params.get("kakao_account");
        return (String) accountMap.get("email");
    }
}
