package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.AddressDTO;
import com.busanit501.shoppingweb_project.security.MemberSecurityDTO;
import com.busanit501.shoppingweb_project.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Log4j2
public class AddressController {

    private final AddressService addressService;

    // 배송지 추가 폼 호출
    @GetMapping("/address-add")
    public String showAddForm(Model model) {
        model.addAttribute("address", new AddressDTO());
        return "address-form";
    }

    // 배송지 수정 폼 호출
    @GetMapping("/address-edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        AddressDTO addressDTO = addressService.getAddressById(id);
        model.addAttribute("address", addressDTO);
        return "address-form";
    }

    // 배송지 추가 처리
    @PostMapping("/address-add")
    public String addAddress(@ModelAttribute AddressDTO addressDTO,
                             @AuthenticationPrincipal MemberSecurityDTO userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        log.info("addAddress - AddressDTO.isDefault = {}", addressDTO.isDefault());
        log.info("addAddress - AddressDTO 내용: {}", addressDTO);

        addressService.addAddress(addressDTO, userDetails.getId());
        return "redirect:/userInfo-update";
    }

    // 배송지 수정 처리
    @PostMapping("/address-edit")
    public String editAddress(@ModelAttribute AddressDTO addressDTO,
                              @AuthenticationPrincipal MemberSecurityDTO userDetails) {

        log.info("editAddress() 호출됨");
        log.info("editAddress - AddressDTO.isDefault = {}", addressDTO.isDefault());

        if (userDetails == null) {
            return "redirect:/login";
        }

        log.info("수정할 주소 ID: " + addressDTO.getId());
        log.info("editAddress - AddressDTO.isDefault = {}", addressDTO.isDefault());
        log.info("editAddress - AddressDTO 내용: {}", addressDTO);

        Long addressId = addressDTO.getId();
        Long memberId = userDetails.getId();

        addressService.updateAddress(addressId, addressDTO, memberId);
        return "redirect:/userInfo-update";
    }
}
