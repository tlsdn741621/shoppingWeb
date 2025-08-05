package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String memberId;
    private String email;
    private String password;
    private String confirmPassword;
    private String userName;
    private String phone;
    private String birthDate;

    private String zipcode;
    private String addressLine;
    private String addressId;
    private boolean registerAddress;

    private Long defaultAddressId;

    private List<AddressDTO> addresses =  new ArrayList<>();

}
