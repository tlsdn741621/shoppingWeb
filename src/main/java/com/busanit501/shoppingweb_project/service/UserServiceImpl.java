package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;

    @Override
    public boolean isMemberIdDuplicated(String memberId) {
        // 아이디 중복 확인
        return memberRepository.findByMemberId(memberId).isPresent();
    }

    @Override
    public void registerMember(Member member) {
        // 회원가입 로직 (예: 비밀번호 암호화 후 저장)
        memberRepository.save(member);
    }
}
