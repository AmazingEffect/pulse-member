package com.pulse.member.config.security.http.user;

import com.pulse.member.adapter.out.persistence.entity.MemberEntity;
import com.pulse.member.adapter.out.persistence.repository.MemberRepository;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsService 인터페이스를 구현하여, UserDetails를 반환하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * @param email 사용자 이메일
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생하는 예외
     * @apiNote 이메일을 기반으로 사용자를 로드하는 메서드 (Spring Security의 UserDetailsService 인터페이스 구현)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl.loadUserByUsername() - getEmail: {}", email);

        // 1. 이메일을 기반으로 사용자 정보를 조회
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.SECURITY_USER_NOT_FOUND));

        // 2. 조회한 엔티티를 사용해서 UserDetailsImpl 객체를 생성하여 반환
        return UserDetailsImpl.fromEntity(memberEntity);
    }

}
