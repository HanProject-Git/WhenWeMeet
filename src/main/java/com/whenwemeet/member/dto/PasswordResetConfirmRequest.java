package com.whenwemeet.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetConfirmRequest(
        @NotBlank(message = "아이디는 필수입니다.")
        String loginId,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "인증코드는 필수입니다.")
        String code,

        @NotBlank(message = "새 비밀번호는 필수입니다.")
        String newPassword
) {
}