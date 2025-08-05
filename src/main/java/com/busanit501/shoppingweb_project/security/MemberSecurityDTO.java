package com.busanit501.shoppingweb_project.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private Long id;

    private String memberId;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;

    private boolean defaultAddressExists;
    private String phone;
    private Map<String, Object> props;

    public MemberSecurityDTO(Long id,String memberId, String password,
                             String email, boolean del, boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(memberId, password, authorities);
        this.id = id;
        this.memberId = memberId;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.props;
    }

    @Override
    public String getName() {
        return this.memberId;
    }

    public Long getMid() {
        return this.id;
    }

    public boolean isKakaoUser() {
        return this.isSocial();
    }

    public boolean isProfileIncomplete() {
        return (this.getPhone() == null || this.getPhone().isEmpty() || !this.isDefaultAddressExists());
    }
}
