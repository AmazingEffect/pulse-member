package com.pulse.member.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pulse.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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

    public static UserDetailsImpl build(Member member) {
        List<GrantedAuthority> authorities = member
                .getRoles()
                .stream()
                .map(roleMap -> new SimpleGrantedAuthority(roleMap.getRole().getName()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getPassword(),
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
