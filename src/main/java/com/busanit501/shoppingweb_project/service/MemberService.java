package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.dto.UserinfoDTO;

public interface MemberService {
    void register(MemberDTO dto);
    void updateMemberInfo(Long memberId, MemberDTO dto);
    void updatePhone(String memberId, String phone);
    boolean isMemberIdDuplicated(String memberId);
    UserinfoDTO getUserinfoDTOById(Long memberId);
    MemberDTO findByMemberId(String memberId);
    Long getMemberPkByMemberId(String memberId);
    Member findByMemberIdWithAddresses(String memberId);
}
