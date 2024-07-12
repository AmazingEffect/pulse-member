package com.pulse.member.controller;

import com.pulse.member.controller.request.RoleCreateRequestDTO;
import com.pulse.member.service.usecase.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member/role")
@RequiredArgsConstructor
@RestController
public class RoleController {

    private final RoleService roleService;

    /**
     * 권한 생성
     *
     * @param roleName 권한명
     * @return ResponseEntity<?>
     */
    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody String roleName) {
        return ResponseEntity.ok(roleService.createRole(roleName));
    }


    /**
     * 권한 여러개 생성
     *
     * @param dto RoleCreateRequestDTO
     * @return ResponseEntity<?>
     */
    @PostMapping("/createRoles")
    public ResponseEntity<?> createRoles(@RequestBody RoleCreateRequestDTO dto) {
        return ResponseEntity.ok(roleService.createRoles(dto));
    }

}
