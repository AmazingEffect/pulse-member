package com.pulse.member.config.security.http.user;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.entity.constant.RoleName;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long memberId;

    private final String email;

    private final String nickname;

    private final Collection<? extends GrantedAuthority> authorities;

    // factory method
    public static UserDetailsImpl fromEntity(MemberEntity memberEntity) {
        // todo: 추후 이 로직을 수정해서 여러개의 권한을 부여할 수 있도록 수정해야함. 지금은 무조건 MEMBER 권한만 부여하도록 설정
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(RoleName.MEMBER.name()));

        // 회원이 존재하지 않을 경우 예외 처리
        if (ObjectUtils.isEmpty(memberEntity)) {
            throw new MemberException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return new UserDetailsImpl(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getNickname(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 보안상 비밀번호는 다루지 않음 (무조건 구현은 해야해서 빈값으로 리턴)
    @Override
    public String getPassword() {
        return "";
    }

    // 유저이름은 따로 없어서 닉네임으로 대체 (닉네임도 고유값)
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
        return Objects.equals(memberId, user.memberId);
    }

}
