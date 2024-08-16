package com.pulse.member.application.service;

import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberResponseDTO;
import com.pulse.member.adapter.out.event.ActivityLogEvent;
import com.pulse.member.adapter.out.event.MemberCreateEvent;
import com.pulse.member.adapter.out.persistence.entity.constant.RoleName;
import com.pulse.member.application.command.auth.ReIssueAccessTokenCommand;
import com.pulse.member.application.command.auth.SignInCommand;
import com.pulse.member.application.command.auth.SignOutCommand;
import com.pulse.member.application.command.auth.SignUpCommand;
import com.pulse.member.application.port.in.auth.AuthUseCase;
import com.pulse.member.application.port.out.member.CreateMemberPort;
import com.pulse.member.application.port.out.member.FindMemberPort;
import com.pulse.member.application.port.out.refreshtoken.CreateRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.DeleteRefreshTokenPort;
import com.pulse.member.application.port.out.refreshtoken.FindRefreshTokenPort;
import com.pulse.member.application.port.out.role.FindRolePort;
import com.pulse.member.application.port.out.role.map.CreateMemberRolePort;
import com.pulse.member.config.jwt.JwtTokenProvider;
import com.pulse.member.config.security.http.user.UserDetailsImpl;
import com.pulse.member.domain.Jwt;
import com.pulse.member.domain.Member;
import com.pulse.member.domain.RefreshToken;
import com.pulse.member.domain.Role;
import com.pulse.member.mapper.JwtMapper;
import com.pulse.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pulse.member.util.Constant.LOGIN;
import static com.pulse.member.util.Constant.LOGOUT;

/**
 * 회원 인증 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 회원 인증, JWT 관련 로직을 처리합니다.
 * Auth service는 member와
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService implements AuthUseCase {

    private final CreateMemberPort createMemberPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;
    private final CreateMemberRolePort createMemberRolePort;
    private final FindMemberPort findMemberPort;
    private final FindRefreshTokenPort findRefreshTokenPort;
    private final FindRolePort findRolePort;
    private final DeleteRefreshTokenPort deleteRefreshTokenPort;

    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberMapper memberMapper;
    private final JwtMapper jwtMapper;

    @Value("${jwt.refreshTokenDurationMinutes}")
    private long refreshTokenDurationMinutes;


    /**
     * @param signInCommand 로그인 요청 도메인
     * @return JWT 토큰 발급 응답 DTO
     * @apiNote 로그인 요청을 처리하는 메서드
     */
    @Transactional
    @Override
    public JwtResponseDTO signInAndPublishJwt(SignInCommand signInCommand) {
        // 1. 로그인 요청 도메인을 생성
        Member member = memberMapper.commandToDomain(signInCommand);

        // 2. db에서 회원 조회
        Member findMember = findMemberPort.findMemberByEmail(member.getEmail());

        // 3. JWT access 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(findMember.getEmail(), findMember.getNickname());

        // 4. JWT refresh 토큰을 생성하고 저장
        RefreshToken refreshToken = RefreshToken.of(findMember, refreshTokenDurationMinutes);
        RefreshToken savedRefreshToken = createRefreshTokenPort.createRefreshToken(refreshToken);

        // 5. JWT 도메인을 생성하고 조회해온 회원 도메인에 저장
        Jwt jwt = Jwt.of(accessToken, savedRefreshToken.getToken(), findMember.getEmail());
        findMember.changeMemberInsideJwt(jwt);

        // 6. 활동 로그 저장 이벤트를 발행하고 JWT 토큰 발급 응답 DTO 반환
        eventPublisher.publishEvent(ActivityLogEvent.of(findMember.getId(), LOGIN));
        return jwtMapper.domainToResponseDTO(jwt);
    }


    /**
     * @return 가입한 회원의 이메일
     * @Param signUpCommand 회원가입 요청 도메인
     * @apiNote 회원 생성 + (이벤트 발행)
     */
    @Transactional
    @Override
    public MemberResponseDTO signUp(SignUpCommand signUpCommand) {
        // 1. 회원가입 요청 도메인을 생성
        Member member = memberMapper.commandToDomain(signUpCommand);

        // 2. 비밀번호 암호화
        member.changePassword(passwordEncoder.encode(member.getPassword()));

        // 3. 회원 저장
        Member savedMember = createMemberPort.createMember(member);

        // 4. 회원 권한을 지정하고 DB에 존재하는게 맞는지 조회한다. (여기서 내가 원하는 권한을 지정해서 저장한다.)
        Role role = Role.of(RoleName.MEMBER.getRoleCode());
        Role findRole = findRolePort.findRoleByName(role);

        // 5. 회원 권한을 저장한다. (회원과 역할의 map 테이블에 저장)
        Long savedMemberRoleId = createMemberRolePort.createMemberRole(savedMember, findRole);

        // 6. MemberCreateEvent 발행하고 회원가입 응답 DTO 반환
        eventPublisher.publishEvent(new MemberCreateEvent(savedMember.getId()));
        return memberMapper.domainToResponseDTO(savedMember);
    }


    /**
     * @param signOutCommand 로그아웃 요청 도메인
     * @apiNote 로그아웃 + JWT 삭제 + 활동 로그 저장 event 발행
     */
    @Transactional
    @Override
    public Long signOut(SignOutCommand signOutCommand) {
        // 1. 로그아웃 요청 도메인을 생성
        Member member = memberMapper.commandToDomain(signOutCommand);

        // 2. 로그아웃 처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 2-1. SecurityContext에서 인증 정보를 가져와서 이메일을 추출
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String email = userDetails.getEmail();
            member.changeEmail(email);

            // 2-2. 회원 조회 후 RefreshToken 삭제
            Member findMember = findMemberPort.findMemberByEmail(member.getEmail());
            deleteRefreshTokenPort.deleteRefreshToken(findMember);

            // 2-3. SecurityContext에서 인증 정보 삭제
            SecurityContextHolder.clearContext();

            // 2-4활동 로그 저장 이벤트 발행
            eventPublisher.publishEvent(ActivityLogEvent.of(member.getId(), LOGOUT));
        }

        // 3. 로그아웃한 회원 ID 반환 (예외가 없으면 로그아웃 성공이라 이 객체의 id를 반환)
        return member.getId();
    }


    /**
     * @param reIssueAccessTokenCommand JWT 토큰 갱신 요청 커맨드
     * @return 갱신된 JWT 토큰
     * @apiNote JWT 토큰 갱신 요청을 처리하는 메서드
     * 클라이언트가 서버에 요청을 보낼 때, 액세스 토큰이 만료된 경우 ExpiredJwtException이 발생합니다.
     * 이 경우, SecurityContextHolder에 인증 정보가 설정되지 않고, 클라이언트는 새로운 액세스 토큰을 발급받기 위해 API를 호출하면 이 메서드가 호출됩니다.
     */
    @Transactional
    @Override
    public JwtResponseDTO reIssueAccessToken(ReIssueAccessTokenCommand reIssueAccessTokenCommand) {
        // 1. refresh 토큰 도메인을 생성
        RefreshToken refreshToken = RefreshToken.of(reIssueAccessTokenCommand.getRefreshToken());

        // 2. 저장된 refresh 토큰 조회 (여기에는 유저 정보가 포함되어 있음)
        RefreshToken findRefreshToken = findRefreshTokenPort.findRefreshToken(refreshToken);

        // 3. refresh 토큰의 유효성 검증 (회원 정보, 만료 날짜 존재여부, 만료 기간 확인)
        findRefreshToken.validRefreshToken();

        // 4. 토큰 내부의 회원정보 추출
        Member member = findRefreshToken.getMember();

        // 5. 새로운 access 토큰 생성
        String newAccessToken = jwtTokenProvider.regenerateAccessToken(member.getEmail(), member.getNickname());

        // 6. 갱신된 JWT 토큰 정보를 DTO에 담아 반환
        return JwtResponseDTO.of(newAccessToken, findRefreshToken.getToken());
    }

}
