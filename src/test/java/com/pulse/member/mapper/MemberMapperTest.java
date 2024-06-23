package com.pulse.member.mapper;

import com.pulse.member.dto.MemberDTO;
import com.pulse.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void testToDto() {
        // Given
        Member member = Member.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("password123")
                .name("John Doe")
                .profilePictureUrl("http://example.com/profile.jpg")
                .introduction("Hello, I'm John!")
                .phoneNumber("123-456-7890")
                .address("123 Main St")
                .birthDate(LocalDateTime.of(1990, 1, 1, 0, 0))
                .gender("Male")
                .website("http://johndoe.com")
                .statusMessage("Feeling good!")
                .accountStatus("Active")
                .joinedDate(LocalDateTime.now().minusYears(1))
                .lastLogin(LocalDateTime.now())
                .build();

        // When
        MemberDTO memberDTO = memberMapper.toDto(member);

        // Then
        assertNotNull(memberDTO);
        assertEquals(member.getId(), memberDTO.getId());
        assertEquals(member.getEmail(), memberDTO.getEmail());
        assertEquals(member.getPassword(), memberDTO.getPassword());
        assertEquals(member.getName(), memberDTO.getName());
        assertEquals(member.getProfilePictureUrl(), memberDTO.getProfilePictureUrl());
        assertEquals(member.getIntroduction(), memberDTO.getIntroduction());
        assertEquals(member.getPhoneNumber(), memberDTO.getPhoneNumber());
        assertEquals(member.getAddress(), memberDTO.getAddress());
        assertEquals(member.getBirthDate(), memberDTO.getBirthDate());
        assertEquals(member.getGender(), memberDTO.getGender());
        assertEquals(member.getWebsite(), memberDTO.getWebsite());
        assertEquals(member.getStatusMessage(), memberDTO.getStatusMessage());
        assertEquals(member.getAccountStatus(), memberDTO.getAccountStatus());
        assertEquals(member.getJoinedDate(), memberDTO.getJoinedDate());
        assertEquals(member.getLastLogin(), memberDTO.getLastLogin());
    }

    @Test
    public void testToEntity() {
        // Given
        MemberDTO memberDTO = new MemberDTO(
                1L,
                "john.doe@example.com",
                "password123",
                "John Doe",
                "http://example.com/profile.jpg",
                "Hello, I'm John!",
                "123-456-7890",
                "123 Main St",
                LocalDateTime.of(1990, 1, 1, 0, 0),
                "Male",
                "http://johndoe.com",
                "Feeling good!",
                "Active",
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now()
        );

        // When
        Member member = memberMapper.toEntity(memberDTO);

        // Then
        assertNotNull(member);
        assertEquals(memberDTO.getId(), member.getId());
        assertEquals(memberDTO.getEmail(), member.getEmail());
        assertEquals(memberDTO.getPassword(), member.getPassword());
        assertEquals(memberDTO.getName(), member.getName());
        assertEquals(memberDTO.getProfilePictureUrl(), member.getProfilePictureUrl());
        assertEquals(memberDTO.getIntroduction(), member.getIntroduction());
        assertEquals(memberDTO.getPhoneNumber(), member.getPhoneNumber());
        assertEquals(memberDTO.getAddress(), member.getAddress());
        assertEquals(memberDTO.getBirthDate(), member.getBirthDate());
        assertEquals(memberDTO.getGender(), member.getGender());
        assertEquals(memberDTO.getWebsite(), member.getWebsite());
        assertEquals(memberDTO.getStatusMessage(), member.getStatusMessage());
        assertEquals(memberDTO.getAccountStatus(), member.getAccountStatus());
        assertEquals(memberDTO.getJoinedDate(), member.getJoinedDate());
        assertEquals(memberDTO.getLastLogin(), member.getLastLogin());
    }

}