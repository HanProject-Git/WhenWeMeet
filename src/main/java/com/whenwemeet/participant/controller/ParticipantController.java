package com.whenwemeet.participant.controller;

import com.whenwemeet.participant.dto.ParticipantResponse;
import com.whenwemeet.participant.service.ParticipantService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/{roomId}/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    public Long joinRoom(
            @PathVariable Long roomId,
            HttpSession session
    ) {
        Long memberId = (Long) session.getAttribute("LOGIN_MEMBER_ID");

        if (memberId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return participantService.joinRoom(roomId, memberId);
    }

    @GetMapping("/me")
    public Long getMyParticipantId(
            @PathVariable Long roomId,
            HttpSession session
    ) {
        Long memberId = (Long) session.getAttribute("LOGIN_MEMBER_ID");

        if (memberId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return participantService.getMyParticipantId(roomId, memberId);
    }

    @GetMapping
    public List<ParticipantResponse> getRoomParticipants(@PathVariable Long roomId) {
        return participantService.getRoomParticipants(roomId);
    }
}