package com.busanit501.shoppingweb_project.mapper;

import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;

import java.time.LocalDateTime;

public class MemberMapper {
    public static Member toMemberEntity(MemberDTO dto, String encodedPassword) {
        return Member.builder()
                .memberId(dto.getMemberId())
                .email(dto.getEmail())
                .password(encodedPassword)
                .userName(dto.getUserName())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .createdAt(LocalDateTime.now())
                .role("ROLE_USER")
                .build();
    }

    // MemberDTO → Address
    public static Address toAddressEntity(MemberDTO dto, Member member) {
        return Address.builder()
                .zipcode(dto.getZipcode())
                .addressLine(dto.getAddressLine())
                .addressId(dto.getAddressId())
                .createdAt(LocalDateTime.now())
                .isDefault(true)
                .member(member)
                .build();
    }
}
