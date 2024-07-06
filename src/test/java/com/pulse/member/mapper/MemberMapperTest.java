package com.pulse.member.mapper;

import com.pulse.member.dto.MemberCreateDTO;
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
    public void testToRetrieveDto() {
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
        MemberCreateDTO memberCreateDTO = memberMapper.toCreateDto(member);

        // Then
        assertNotNull(memberCreateDTO);
        assertEquals(member.getId(), memberCreateDTO.getId());
        assertEquals(member.getEmail(), memberCreateDTO.getEmail());
        assertEquals(member.getPassword(), memberCreateDTO.getPassword());
        assertEquals(member.getName(), memberCreateDTO.getName());
        assertEquals(member.getProfilePictureUrl(), memberCreateDTO.getProfilePictureUrl());
        assertEquals(member.getIntroduction(), memberCreateDTO.getIntroduction());
        assertEquals(member.getPhoneNumber(), memberCreateDTO.getPhoneNumber());
        assertEquals(member.getAddress(), memberCreateDTO.getAddress());
        assertEquals(member.getBirthDate(), memberCreateDTO.getBirthDate());
        assertEquals(member.getGender(), memberCreateDTO.getGender());
        assertEquals(member.getWebsite(), memberCreateDTO.getWebsite());
        assertEquals(member.getStatusMessage(), memberCreateDTO.getStatusMessage());
        assertEquals(member.getAccountStatus(), memberCreateDTO.getAccountStatus());
        assertEquals(member.getJoinedDate(), memberCreateDTO.getJoinedDate());
        assertEquals(member.getLastLogin(), memberCreateDTO.getLastLogin());
    }

    @Test
    public void testToEntity() {
        // Given
        MemberCreateDTO memberCreateDTO = new MemberCreateDTO(
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
        Member member = memberMapper.toEntity(memberCreateDTO);

        // Then
        assertNotNull(member);
        assertEquals(memberCreateDTO.getId(), member.getId());
        assertEquals(memberCreateDTO.getEmail(), member.getEmail());
        assertEquals(memberCreateDTO.getPassword(), member.getPassword());
        assertEquals(memberCreateDTO.getName(), member.getName());
        assertEquals(memberCreateDTO.getProfilePictureUrl(), member.getProfilePictureUrl());
        assertEquals(memberCreateDTO.getIntroduction(), member.getIntroduction());
        assertEquals(memberCreateDTO.getPhoneNumber(), member.getPhoneNumber());
        assertEquals(memberCreateDTO.getAddress(), member.getAddress());
        assertEquals(memberCreateDTO.getBirthDate(), member.getBirthDate());
        assertEquals(memberCreateDTO.getGender(), member.getGender());
        assertEquals(memberCreateDTO.getWebsite(), member.getWebsite());
        assertEquals(memberCreateDTO.getStatusMessage(), member.getStatusMessage());
        assertEquals(memberCreateDTO.getAccountStatus(), member.getAccountStatus());
        assertEquals(memberCreateDTO.getJoinedDate(), member.getJoinedDate());
        assertEquals(memberCreateDTO.getLastLogin(), member.getLastLogin());
    }

}