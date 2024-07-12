package com.pulse.member.config.security;

import com.pulse.member.entity.Member;
import com.pulse.member.repository.MemberRepository;
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
        Member member = memberRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원의 이메일을 찾을 수 없습니다. : " + email));

        // 2. UserDetailsImpl 객체 반환
        return UserDetailsImpl.build(member);
    }

}
