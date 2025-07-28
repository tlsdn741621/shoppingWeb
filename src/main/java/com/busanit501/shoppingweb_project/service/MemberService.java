package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.dto.MemberDTO;

public interface MemberService {
    void register(MemberDTO dto);
    boolean isMemberIdDuplicated(String memberId);
}
