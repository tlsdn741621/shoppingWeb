package com.busanit501.shoppingweb_project.security;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with memberId: " + username));
        Collection<? extends GrantedAuthority> authorities = List.of(() -> member.getRole());

        MemberSecurityDTO securityDTO = new MemberSecurityDTO(
                member.getId(),
                member.getMemberId(),
                member.getPassword(),
                member.getEmail(),
                false,
                false,
                authorities
        );

        securityDTO.setPhone(member.getPhone());
        securityDTO.setDefaultAddressExists(member.getAddresses() != null &&
                member.getAddresses().stream().anyMatch(Address::isDefault));

        return securityDTO;
    }
}
