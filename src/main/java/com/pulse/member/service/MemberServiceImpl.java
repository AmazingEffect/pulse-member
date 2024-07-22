package com.pulse.member.service;

import com.pulse.member.controller.request.LogoutRequestDTO;
import com.pulse.member.controller.request.MemberRetrieveDTO;
import com.pulse.member.controller.request.MemberSignUpRequestDTO;
import com.pulse.member.entity.Member;
import com.pulse.member.entity.MemberRole;
import com.pulse.member.entity.Role;
import com.pulse.member.entity.constant.RoleName;
import com.pulse.member.exception.ErrorCode;
import com.pulse.member.listener.spring.event.MemberCreateEvent;
import com.pulse.member.listener.spring.event.NicknameChangeEvent;
import com.pulse.member.mapper.MemberMapper;
import com.pulse.member.repository.MemberRepository;
import com.pulse.member.repository.MemberRoleRepository;
import com.pulse.member.repository.RoleRepository;
import com.pulse.member.service.usecase.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;
    private final MemberMapper memberMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;


    /**
     * 회원 생성 + (이벤트 발행)
     *
     * @param signUpRequestDTO 회원 가입 요청 DTO
     */
    @Transactional
    @Override
    public MemberSignUpRequestDTO register(MemberSignUpRequestDTO signUpRequestDTO) {
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
     * ID로 회원 조회
     *
     * @param id 회원 ID
     * @return 회원 조회 DTO
     */
    @Override
    public MemberRetrieveDTO getMemberById(Long id) {
        return memberRepository.findById(id)
                .map(memberMapper::toRetrieveDto)
                .orElseThrow(() -> new RuntimeException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }


    /**
     * 이메일로 회원 조회
     *
     * @param email 이메일
     * @return 회원 조회 DTO
     */
    @Override
    public MemberRetrieveDTO getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(memberMapper::toRetrieveDto)
                .orElseThrow(() -> new RuntimeException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }


    /**
     * 닉네임 변경
     *
     * @param id          회원 ID
     * @param newNickname 변경할 닉네임
     */
    @Override
    public Long changeNickname(Long id, String newNickname) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        member.changeNickname(newNickname);
        memberRepository.save(member);

        // NicknameChangeEvent 발행
        eventPublisher.publishEvent(new NicknameChangeEvent(member.getId()));

        return 1L;
    }


    @Override
    public void logout(LogoutRequestDTO logoutRequest) {

    }

}
