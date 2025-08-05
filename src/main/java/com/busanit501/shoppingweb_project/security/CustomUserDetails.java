//package com.busanit501.shoppingweb_project.security;
//
//import com.busanit501.shoppingweb_project.domain.Member;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//
//public class CustomUserDetails implements UserDetails {
//
//    private final Member member;
//
//    public CustomUserDetails(Member member) {
//        this.member = member;
//    }
//
//    public Member getMember() {
//        return member;
//    }
//
//    @Override // 사용자에게 권한 부여
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole()));
//    }
//    public Long getMemberId(){
//        return member.getId();
//    }
//
//    @Override
//    public String getPassword() {
//        return member.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return member.getMemberId(); // 로그인에 사용하는 아이디 필드
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//
//}
