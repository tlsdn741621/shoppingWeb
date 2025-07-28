package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.mapper.MemberMapper;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(MemberDTO dto) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // DTO → Member 변환
        Member member = MemberMapper.toMemberEntity(dto, encodedPassword);

        // 기본 권한 세팅
        member.setRole("ROLE_USER");

        // 주소가 있다면 Address도 생성 후 관계 연결
        if (dto.isRegisterAddress()) {
            Address address = MemberMapper.toAddressEntity(dto, member);
            member.getAddresses().add(address); // 양방향일 경우
        }

        memberRepository.save(member);
    }

    @Override
    public boolean isMemberIdDuplicated(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }
}