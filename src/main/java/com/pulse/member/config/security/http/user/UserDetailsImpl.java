package com.pulse.member.config.security.http.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.entity.constant.RoleName;
import com.pulse.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String email;

    private final String nickname;

    @JsonIgnore // 이 어노테이션은 JSON으로 변환될 때, password를 제외시킴
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(MemberEntity memberEntity) {
        // todo: 추후 이 로직을 수정해서 여러개의 권한을 부여할 수 있도록 수정 지금은 무조건 MEMBER 권한만 부여
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(RoleName.MEMBER.name()));

        return new UserDetailsImpl(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getNickname(),
                memberEntity.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String email() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }


}
