package com.pulse.member.config.security.http.user;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService 인터페이스를 구현하여, UserDetails를 반환하는 클래스
 */
@Slf4j
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl.loadUserByUsername() - email: {}", email);

        // 1. 이메일을 기반으로 사용자 로드
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원의 이메일을 찾을 수 없습니다. : " + email));

        // 2. UserDetailsImpl 객체 반환
        return UserDetailsImpl.build(memberEntity);
    }

}
