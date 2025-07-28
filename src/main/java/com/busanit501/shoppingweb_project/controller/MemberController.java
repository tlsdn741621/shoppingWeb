package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String registerForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "/signup";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute MemberDTO memberDTO,
                           BindingResult bindingResult,
                           Model model) {
        // 유효성 검사 실패
        if (bindingResult.hasErrors()) {
            return "/signup";
        }

        // 아이디 중복 확인
        if (memberService.isMemberIdDuplicated(memberDTO.getMemberId())) {
            model.addAttribute("errorMessage", "이미 존재하는 아이디입니다.");
            return "/signup";
        }

        // 비밀번호 확인
        if (!memberDTO.getPassword().equals(memberDTO.getConfirmPassword())) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "/signup";
        }

        // 회원 등록
        memberService.register(memberDTO);
        return "redirect:/login";
    }
}
