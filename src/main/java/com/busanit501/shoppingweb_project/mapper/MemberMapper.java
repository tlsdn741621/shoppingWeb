package com.busanit501.shoppingweb_project.mapper;

import com.busanit501.shoppingweb_project.dto.AddressDTO;
import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MemberMapper {
    public static Member toMemberEntity(MemberDTO dto, String encodedPassword) {
        LocalDate birthDate = null;
        try {
            if (dto.getBirthDate() != null && !dto.getBirthDate().isEmpty()) {
                birthDate = LocalDate.parse(dto.getBirthDate());
            }
        } catch (DateTimeParseException e) {
            System.out.println("birthDate 파싱 실패: " + dto.getBirthDate());
        }

        return Member.builder()
                .memberId(dto.getMemberId())
                .email(dto.getEmail())
                .password(encodedPassword)
                .userName(dto.getUserName())
                .phone(dto.getPhone())
                .birthDate(birthDate)   // 여기서 변수 사용함
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

    public static MemberDTO toMemberDTO(Member member) {
        List<AddressDTO> addressDTOs = new ArrayList<>();
        if (member.getAddresses() != null) {
            for (Address addr : member.getAddresses()) {
                AddressDTO dto = AddressDTO.builder()
                        .id(addr.getId())
                        .zipcode(addr.getZipcode())
                        .addressLine(addr.getAddressLine())
                        .addressId(addr.getAddressId())
                        .isDefault(addr.isDefault())
                        .build();
                addressDTOs.add(dto);
            }
        }

        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .userName(member.getUserName())
                .phone(member.getPhone())
                .birthDate(member.getBirthDate() != null ? member.getBirthDate().toString() : "")
                .addresses(addressDTOs)
                // 나머지 필드도 필요하면 추가
                .build();
    }
}
