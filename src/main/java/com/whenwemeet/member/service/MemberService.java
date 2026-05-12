package com.whenwemeet.member.service;

import com.whenwemeet.member.dto.MemberResponse;
import com.whenwemeet.member.dto.SignupRequest;
import com.whenwemeet.member.entity.Member;
import com.whenwemeet.member.repository.MemberRepository;
import com.whenwemeet.participant.entity.Participant;
import com.whenwemeet.participant.repository.ParticipantRepository;
import com.whenwemeet.availability.repository.AvailableDateRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.whenwemeet.member.dto.LoginRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;
    private final AvailableDateRepository availableDateRepository;

    public MemberResponse signup(SignupRequest request) {
        if (memberRepository.existsByLoginId(request.loginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        Member member = new Member(
                request.loginId(),
                request.password(),
                request.name()
        );

        Member savedMember = memberRepository.save(member);

        return MemberResponse.from(savedMember);
    }

    public MemberResponse login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!member.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return MemberResponse.from(member);
    }

    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return MemberResponse.from(member);
    }

    @Transactional
    public void deleteMe(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<Participant> participants =
                participantRepository.findByMember_Id(memberId);

        for (Participant participant : participants) {

            availableDateRepository.deleteByParticipantId(
                    participant.getId()
            );

            participantRepository.delete(participant);
        }

        memberRepository.delete(member);
    }
}