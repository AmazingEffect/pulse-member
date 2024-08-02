package com.pulse.member.application;

import com.pulse.member.config.jwt.JwtTokenProvider;
import com.pulse.member.config.security.http.user.UserDetailsImpl;
import com.pulse.member.adapter.in.web.dto.request.LoginRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.LogoutRequestDTO;
import com.pulse.member.adapter.in.web.dto.request.MemberSignUpRequestDTO;
import com.pulse.member.adapter.in.web.dto.response.JwtResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberReadResponseDTO;
import com.pulse.member.adapter.in.web.dto.response.MemberSignUpResponseDTO;
import com.pulse.member.adapter.out.persistence.entity.Member;
import com.pulse.member.adapter.out.persistence.entity.MemberRole;
import com.pulse.member.adapter.out.persistence.entity.RefreshToken;
import com.pulse.member.adapter.out.persistence.entity.Role;
import com.pulse.member.adapter.out.persistence.entity.constant.RoleName;
import com.pulse.member.adapter.out.event.ActivityLogEvent;
import com.pulse.member.adapter.out.event.MemberCreateEvent;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.adapter.out.persistence.repository.MemberRepository;
import com.pulse.member.adapter.out.persistence.repository.MemberRoleRepository;
import com.pulse.member.adapter.out.persistence.repository.RoleRepository;
import com.pulse.member.application.port.in.AuthUseCase;
import com.pulse.member.application.port.in.JwtUseCase;
import com.pulse.member.application.port.in.MemberUseCase;
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

    private final MemberUseCase memberUseCase;
    private final JwtUseCase jwtUseCase;

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    private final MemberMapper memberMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 로그인 요청을 처리하는 메서드
     *
     * @param loginRequest 로그인 요청 DTO
     * @return JWT 토큰 발급 응답 DTO
     */
    @Transactional
    @Override
    public JwtResponseDTO signInAndMakeJwt(LoginRequestDTO loginRequest) {
        // 1. authentication 객체를 생성하고 SecurityContext에 저장
        Authentication authentication = createAuthenticationFrom(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. JWT access 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        // 3. refresh 토큰 생성
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getEmail();
        MemberReadResponseDTO memberDTO = memberUseCase.getMemberByEmail(email);
        RefreshToken refreshToken = jwtUseCase.createRefreshToken(memberDTO);

        // 4. JWT 토큰 발급 응답 DTO 생성
        JwtResponseDTO jwtResponse = createJwtResponseDTOFrom(accessToken, refreshToken, email, userDetails);

        // 5. 활동 로그 저장 이벤트 발행
        eventPublisher.publishEvent(ActivityLogEvent.of(memberDTO.getId(), LOGIN));
        return jwtResponse;
    }


    /**
     * 회원 생성 + (이벤트 발행)
     *
     * @param signUpRequestDTO 회원 가입 요청 DTO
     * @return 가입한 회원의 이메일
     */
    @Transactional
    @Override
    public MemberSignUpResponseDTO signUpAndPublishEvent(MemberSignUpRequestDTO signUpRequestDTO) {
        // 1. 비밀번호 암호화
        signUpRequestDTO.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        Member member = memberMapper.toEntity(signUpRequestDTO);

        // 2. 회원 저장
        Member savedMember = memberRepository.saveAndFlush(member);

        // 3. 회원 권한을 찾아와서 저장
        Role role = roleRepository.findByName(RoleName.MEMBER.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        memberRoleRepository.save(MemberRole.of(savedMember, role));

        // 4. MemberCreateEvent 발행
        eventPublisher.publishEvent(new MemberCreateEvent(savedMember.getId()));
        return memberMapper.toCreateDto(savedMember);
    }


    /**
     * 로그아웃 + JWT 삭제 + 활동 로그 저장 event 발행
     *
     * @param logoutRequest 로그아웃 요청 DTO
     */
    @Transactional
    @Override
    public void signOutAndDeleteJwt(LogoutRequestDTO logoutRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // 1. SecurityContext에서 인증 정보를 가져와서 이메일을 추출
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String email = userDetails.getEmail();

            // 2. 회원 조회 후 RefreshToken 삭제
            MemberReadResponseDTO memberDTO = memberUseCase.getMemberByEmail(email);
            jwtUseCase.deleteByMember(memberDTO);

            // 3. SecurityContext에서 인증 정보 삭제
            SecurityContextHolder.clearContext();

            // 활동 로그 저장 이벤트 발행
            eventPublisher.publishEvent(ActivityLogEvent.of(memberDTO.getId(), LOGOUT));
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
        RefreshToken refreshToken = jwtUseCase.findByToken(requestRefreshToken);
        jwtUseCase.verifyExpiration(refreshToken);

        // 2. access 토큰 재발급
        String email = refreshToken.getMember().getEmail();
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);
        String newAccessToken = jwtTokenProvider.generateAccessToken(authenticationToken);

        // 3. SecurityContextHolder에 새로운 인증 정보를 설정합니다.
        // todo: 여기서 authorities가 null일것같은데 get하면 NPE 발생하지 않나?
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.of(newAccessToken, requestRefreshToken, email, authenticationToken.getAuthorities());
        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.of(newAccessToken, requestRefreshToken, email);

        // 4. 활동 로그 저장 이벤트 발행
        eventPublisher.publishEvent(ActivityLogEvent.of(refreshToken.getMember().getId(), REISSUE_REFRESH_TOKEN));
        return jwtResponseDTO;
    }


    /**
     * 로그인 요청을 처리하기 위해 Authentication 객체를 생성하는 메서드
     *
     * @param loginRequest 로그인 요청 DTO
     * @return 생성된 Authentication 객체
     */
    private Authentication createAuthenticationFrom(LoginRequestDTO loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
    }


    /**
     * JWT 토큰 발급 응답 DTO 생성
     *
     * @param accessToken  JWT access 토큰
     * @param refreshToken RefreshToken
     * @param email        이메일
     * @param userDetails  UserDetailsImpl
     * @return JWT 토큰 발급 응답 DTO
     */
    private static JwtResponseDTO createJwtResponseDTOFrom(String accessToken, RefreshToken refreshToken, String email, UserDetailsImpl userDetails) {
        return JwtResponseDTO.of(
                accessToken,
                refreshToken.getToken(),
                email,
                userDetails.getAuthorities()
        );
    }

}
