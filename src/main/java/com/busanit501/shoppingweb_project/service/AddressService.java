package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<Address> getAddressesByMemberId(Long memberId);
    Address addAddress(AddressDTO dto, Long memberId);
    Address updateAddress(Long addressId, AddressDTO dto, Long authenticatedMemberId);
    void setDefaultAddress(Long memberId, Long addressId);
    AddressDTO getAddressById(Long id);
}
