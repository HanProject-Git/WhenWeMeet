package com.whenwemeet.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private LocalDateTime createdAt;

    private String provider;

    @Column(nullable = false, unique = true)
    private String email;

    public Member(String loginId, String password, String name, String email) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.provider = "LOCAL";
    }

    public static Member socialMember(
            String loginId,
            String name,
            String provider
    ) {
        Member member = new Member();

        member.loginId = loginId;
        member.password = "SOCIAL_LOGIN";
        member.name = name;
        member.email = loginId;
        member.provider = provider;

        return member;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void changePassword(String password) {
        this.password = password;
    }
}