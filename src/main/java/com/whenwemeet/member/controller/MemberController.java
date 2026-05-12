package com.whenwemeet.member.controller;

import com.whenwemeet.member.dto.LoginRequest;
import com.whenwemeet.member.dto.MemberResponse;
import com.whenwemeet.member.dto.SignupRequest;
import com.whenwemeet.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public MemberResponse signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return memberService.signup(request);
    }

    @PostMapping("/login")
    public MemberResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        MemberResponse member = memberService.login(request);

        session.setAttribute("LOGIN_MEMBER_ID", member.id());

        return member;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/me")
    public MemberResponse me(HttpSession session) {
        
        Long memberId = (Long) session.getAttribute("LOGIN_MEMBER_ID");

        if (memberId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return memberService.getMember(memberId);
    }

    @DeleteMapping("/me")
    public void deleteMe(HttpSession session) {

        Long memberId = (Long) session.getAttribute("LOGIN_MEMBER_ID");

        if (memberId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        memberService.deleteMe(memberId);

        session.invalidate();
    }
}