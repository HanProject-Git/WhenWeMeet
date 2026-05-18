package com.whenwemeet.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetCode(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    false,
                    "UTF-8"
            );

            helper.setFrom(fromEmail, "WhenWeMeet");
            helper.setTo(toEmail);
            helper.setSubject("[WhenWeMeet] 비밀번호 재설정 인증코드");
            helper.setText(
                    "비밀번호 재설정 인증코드는 다음과 같습니다.\n\n" +
                            code +
                            "\n\n인증코드를 입력하고 새 비밀번호를 설정해주세요.",
                    false
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }

    public void sendLoginId(String toEmail, String loginId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    false,
                    "UTF-8"
            );

            helper.setFrom(fromEmail, "WhenWeMeet");
            helper.setTo(toEmail);
            helper.setSubject("[WhenWeMeet] 아이디 찾기 안내");
            helper.setText(
                    "요청하신 WhenWeMeet 아이디는 다음과 같습니다.\n\n" +
                            loginId +
                            "\n\n본인이 요청하지 않았다면 이 메일을 무시해주세요.",
                    false
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }

    public void sendSignupVerificationCode(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    false,
                    "UTF-8"
            );

            helper.setFrom(fromEmail, "WhenWeMeet");
            helper.setTo(toEmail);
            helper.setSubject("[WhenWeMeet] 회원가입 이메일 인증코드");
            helper.setText(
                    "WhenWeMeet 회원가입 인증코드는 다음과 같습니다.\n\n" +
                            code +
                            "\n\n인증코드를 입력하면 회원가입을 계속 진행할 수 있습니다.",
                    false
            );

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }
}