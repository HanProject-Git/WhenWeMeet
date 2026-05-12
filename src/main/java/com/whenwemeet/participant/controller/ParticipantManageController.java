package com.whenwemeet.participant.controller;

import com.whenwemeet.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participants")
public class ParticipantManageController {

    private final ParticipantService participantService;

    @DeleteMapping("/{participantId}")
    public void deleteParticipant(
            @PathVariable Long participantId
    ) {
        participantService.deleteParticipant(participantId);
    }
}