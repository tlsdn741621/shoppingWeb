package com.busanit501.shoppingweb_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Long id; // Address의 기본 키

    private String addressId;     // 도로명 주소
    private String addressLine;   // 상세 주소
    private String zipcode;       // 우편번호

    private boolean isDefault;    // 기본 배송지 여부

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
