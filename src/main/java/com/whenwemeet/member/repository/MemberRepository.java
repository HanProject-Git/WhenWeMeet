package com.whenwemeet.member.repository;

import com.whenwemeet.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndName(String email, String name);

    Optional<Member> findByLoginIdAndEmail(String loginId, String email);
}