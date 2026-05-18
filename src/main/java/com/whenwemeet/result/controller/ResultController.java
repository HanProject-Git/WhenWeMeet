package com.whenwemeet.result.controller;

import com.whenwemeet.result.dto.DateParticipantResponse;
import com.whenwemeet.result.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import com.whenwemeet.result.dto.PendingParticipantResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/{roomId}/result")
public class ResultController {

    private final ResultService resultService;

    @GetMapping
    public List<DateParticipantResponse> getResult(
            @PathVariable Long roomId
    ) {
        return resultService.getResult(roomId);
    }

    @GetMapping("/common")
    public List<LocalDate> getCommonAvailableDates(
            @PathVariable Long roomId
    ) {
        return resultService.getCommonAvailableDates(roomId);
    }

    @GetMapping("/pending")
    public List<PendingParticipantResponse> getPendingParticipants(
            @PathVariable Long roomId
    ) {
        return resultService.getPendingParticipants(roomId);
    }
}