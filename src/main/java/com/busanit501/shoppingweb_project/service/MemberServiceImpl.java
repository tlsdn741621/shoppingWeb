package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.dto.UserinfoDTO;
import com.busanit501.shoppingweb_project.mapper.MemberMapper;
import com.busanit501.shoppingweb_project.repository.AddressRepository;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final MemberMapper memberMapper;
    private final ModelMapper modelMapper;

    @Override
    public void register(MemberDTO dto) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // DTO → Member 변환
        Member member = MemberMapper.toMemberEntity(dto, encodedPassword);

        // 기본 권한 세팅
        boolean isAdmin = false;

        if (isAdmin) {
            member.settingRole("ROLE_ADMIN");
        } else {
            member.settingRole("ROLE_USER");
        }

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

    @Override
    public UserinfoDTO getUserinfoDTOById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("해당 ID를 가진 회원 정보를 찾을 수 없습니다: " + memberId));
        Address address = addressRepository.findByMemberAndIsDefaultTrue(member);
        UserinfoDTO dto = UserinfoDTO.toUserinfoDTO(member, address);
        return dto;
    }

    @Override
    @Transactional
    public void updateMemberInfo(Long memberId, MemberDTO dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            // 비밀번호 암호화 처리 (예: BCryptPasswordEncoder 사용)
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            member.setPassword(encodedPassword);
        }

        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());

        // 기본 배송지 변경 처리 (예: memberDTO에 기본배송지 id가 있다면)
        if (dto.getDefaultAddressId() != null) {
            Long memberPk = member.getId();
            addressRepository.resetDefaultAddress(memberPk);
            Address defaultAddress = addressRepository.findById(dto.getDefaultAddressId())
                    .orElseThrow(() -> new NoSuchElementException("기본 배송지 정보를 찾을 수 없습니다."));
            defaultAddress.setIsDefault(true);
            addressRepository.save(defaultAddress);
        }

        memberRepository.save(member);
    }

    @Override
    public MemberDTO findByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다."));

        MemberDTO dto = modelMapper.map(member, MemberDTO.class);

        // birthDate null 체크 후 빈 문자열 처리
        if (member.getBirthDate() == null) {
            dto.setBirthDate("");
        } else {
            dto.setBirthDate(member.getBirthDate().toString());
        }

        return dto;
    }

    @Override
    public void updatePhone(String memberId, String phone) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 없음"));
        member.setPhone(phone);
        memberRepository.save(member);
    }

    @Override
    public Long getMemberPkByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 없음"))
                .getId();
    }

    @Override
    public Member findByMemberIdWithAddresses(String memberId) {
        return memberRepository.findByMemberIdWithAddresses(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

}