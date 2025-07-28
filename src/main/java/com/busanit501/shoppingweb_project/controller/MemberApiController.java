package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/check-id")
    public Map<String, Boolean> checkIdDuplicate(@RequestParam String memberId) {
        if(memberId == null || memberId.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "memberId는 필수입니다.");
        }
        try {
            boolean isDuplicate = memberService.isMemberIdDuplicated(memberId);
            return Collections.singletonMap("duplicate", isDuplicate);
        } catch (Exception e) {
            e.printStackTrace();  // 에러 로그 출력
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

}
