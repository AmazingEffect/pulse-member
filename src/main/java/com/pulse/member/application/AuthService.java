package com.pulse.member.application;

import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.out.event.ActivityLogEvent;
import com.pulse.member.adapter.out.event.MemberCreateEvent;
import com.pulse.member.adapter.out.persistence.entity.constant.RoleName;
import com.pulse.member.application.port.in.auth.AuthUseCase;
import com.pulse.member.application.port.out.member.CreateMemberPort;
import com.pulse.member.application.port.out.member.FindMemberPort;
import com.pulse.member.application.port.out.role.FindRolePort;
import com.pulse.member.application.port.out.role.map.CreateMemberRolePort;
import com.pulse.member.config.jwt.JwtTokenProvider;
import com.pulse.member.config.security.http.user.UserDetailsImpl;
import com.pulse.member.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.pulse.member.util.Constant.*;

/**
 * 회원 인증 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 회원 인증, JWT 관련 로직을 처리합니다.
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService implements AuthUseCase {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final CreateMemberPort createMemberPort;
    private final FindRolePort findRolePort;
    private final CreateMemberRolePort createMemberRolePort;
    private final FindMemberPort findMemberPort;

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 로그인 요청을 처리하는 메서드
     *
     * @param member 로그인 요청 도메인
     * @return JWT 토큰 발급 응답 DTO
     */
    @Transactional
    @Override
    public Member signIn(Member member) {
        // 1. authentication 객체를 생성하고 SecurityContext에 저장
        Authentication authentication = createAuthenticationFrom(member);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. JWT access 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        // 3. refresh 토큰 생성
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        member.changeEmail(email);
        Member findMember = memberService.findMemberByEmail(member);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(findMember);

        // 4. JWT 토큰 발급 응답 DTO 생성
        Jwt jwt = createJwtResponseDomain(accessToken, refreshToken, email, userDetails);
        findMember.changeJwt(jwt);

        // 5. 활동 로그 저장 이벤트 발행
        eventPublisher.publishEvent(ActivityLogEvent.of(member.getId(), LOGIN));
        return findMember;
    }


    /**
     * 회원 생성 + (이벤트 발행)
     *
     * @param member 회원 생성 요청 도메인
     * @return 가입한 회원의 이메일
     */
    @Transactional
    @Override
    public Member signUp(Member member) {
        // 1. 비밀번호 암호화
        member.changePassword(passwordEncoder.encode(member.getPassword()));

        // 2. 회원 저장
        Member savedMember = createMemberPort.createMember(member);

        // 3. 회원 권한을 지정하고 DB에 존재하는게 맞는지 조회한다. (여기서 내가 원하는 권한을 지정해서 저장한다.)
        Role role = Role.of(RoleName.MEMBER.getRoleCode());
        Role findRole = findRolePort.findRoleByName(role);

        // 4. 회원 권한을 저장한다. (회원과 역할의 map 테이블에 저장)
        MemberRole memberRole = createMemberRolePort.createMemberRole(savedMember, findRole);

        // 4. MemberCreateEvent 발행
        eventPublisher.publishEvent(new MemberCreateEvent(savedMember.getId()));
        return savedMember;
    }


    /**
     * 로그아웃 + JWT 삭제 + 활동 로그 저장 event 발행
     *
     * @param member 로그아웃 요청 도메인
     */
    @Transactional
    @Override
    public void signOut(Member member) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 1. SecurityContext에서 인증 정보를 가져와서 이메일을 추출
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String email = userDetails.getEmail();
            member.changeEmail(email);

            // 2. 회원 조회 후 RefreshToken 삭제
            Member findMember = findMemberPort.findMemberByEmail(member);
            refreshTokenService.deleteRefreshToken(findMember);

            // 3. SecurityContext에서 인증 정보 삭제
            SecurityContextHolder.clearContext();

            // 활동 로그 저장 이벤트 발행
            eventPublisher.publishEvent(ActivityLogEvent.of(member.getId(), LOGOUT));
        }
    }


    /**
     * JWT 토큰 갱신 요청을 처리하는 메서드
     *
     * @param request 요청 파라미터
     * @return 갱신된 JWT 토큰
     */
    @Transactional
    @Override
    public JwtResponseDTO reIssueRefreshToken(Map<String, String> request) {
        // 1. refresh 토큰을 조회하고 만료 여부를 확인
        String requestRefreshToken = request.get(REFRESH_TOKEN);
        RefreshToken findRefreshToken = refreshTokenService.findRefreshToken(RefreshToken.of(requestRefreshToken));
        findRefreshToken.verifyTokenExpiration();

        // 2. access 토큰 재발급
        String email = findRefreshToken.getMember().getEmail();
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);
        String newAccessToken = jwtTokenProvider.generateAccessToken(authenticationToken);

        // 3. SecurityContextHolder에 새로운 인증 정보를 설정합니다.
        // todo: 여기서 authorities가 null일것같은데 get하면 NPE 발생하지 않나?
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.of(newAccessToken, requestRefreshToken, email);
//        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.of(newAccessToken, requestRefreshToken, email, authenticationToken.getAuthorities());

        // 4. 활동 로그 저장 이벤트 발행
        eventPublisher.publishEvent(ActivityLogEvent.of(findRefreshToken.getMember().getId(), REISSUE_REFRESH_TOKEN));
        return jwtResponseDTO;
    }


    /**
     * 로그인 요청을 처리하기 위해 Authentication 객체를 생성하는 메서드
     *
     * @param member 로그인 요청 도메인
     * @return 생성된 Authentication 객체
     */
    private Authentication createAuthenticationFrom(Member member) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword())
        );
    }


    /**
     * JWT 토큰 발급 응답 DTO 생성
     *
     * @param accessToken  JWT access 토큰
     * @param refreshToken JWT refresh 토큰
     * @param email        이메일
     * @param userDetails  UserDetailsImpl
     * @return JWT 토큰 발급 응답 DTO
     */
    private Jwt createJwtResponseDomain(String accessToken, RefreshToken refreshToken, String email, UserDetailsImpl userDetails) {
        return Jwt.of(accessToken, refreshToken.getToken(), email, userDetails.getAuthorities());
    }

}
