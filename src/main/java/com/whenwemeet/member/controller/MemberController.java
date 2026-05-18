package com.whenwemeet.member.controller;

import com.whenwemeet.member.dto.LoginRequest;
import com.whenwemeet.member.dto.MemberResponse;
import com.whenwemeet.member.dto.SignupRequest;
import com.whenwemeet.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.whenwemeet.member.dto.PasswordResetCodeRequest;
import com.whenwemeet.member.dto.PasswordResetVerifyRequest;
import com.whenwemeet.member.dto.PasswordResetConfirmRequest;
import com.whenwemeet.member.dto.FindLoginIdRequest;
import com.whenwemeet.member.dto.EmailVerificationCodeRequest;
import com.whenwemeet.member.dto.EmailVerificationVerifyRequest;

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

    @PostMapping("/password-reset/code")
    public void sendPasswordResetCode(
            @Valid @RequestBody PasswordResetCodeRequest request
    ) {
        memberService.sendPasswordResetCode(request);
    }

    @PostMapping("/password-reset/verify")
    public void verifyPasswordResetCode(
            @Valid @RequestBody PasswordResetVerifyRequest request
    ) {
        memberService.verifyPasswordResetCode(request);
    }

    @PostMapping("/password-reset/confirm")
    public void resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest request
    ) {
        memberService.resetPassword(request);
    }

    @PostMapping("/find-login-id")
    public void findLoginId(
            @Valid @RequestBody FindLoginIdRequest request
    ) {
        memberService.sendLoginIdToEmail(request);
    }

    @PostMapping("/signup/email/code")
    public void sendSignupEmailCode(
            @Valid @RequestBody EmailVerificationCodeRequest request
    ) {
        memberService.sendSignupEmailCode(request);
    }

    @PostMapping("/signup/email/verify")
    public void verifySignupEmailCode(
            @Valid @RequestBody EmailVerificationVerifyRequest request
    ) {
        memberService.verifySignupEmailCode(request);
    }
}