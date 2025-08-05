package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserinfoDTO {
    private Long id;
    private String memberId;
    private String email;
    private String userName;
    private String phone;
    private String birthDate;

    private String addressLine;
    private String addressId;

    public static UserinfoDTO toUserinfoDTO(Member member, Address address){
        return UserinfoDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .userName(member.getUserName())
                .phone(member.getPhone())
                // birthDate가 null이면 빈 문자열, 아니면 문자열로 변환
                .birthDate(member.getBirthDate() != null ? member.getBirthDate().toString() : "")
                // address가 null일 수도 있으니 체크
                .addressLine(address != null ? address.getAddressLine() : "")
                .addressId(address != null ? address.getAddressId() : "")
                .id(member.getId())
                .build();
    }
}
