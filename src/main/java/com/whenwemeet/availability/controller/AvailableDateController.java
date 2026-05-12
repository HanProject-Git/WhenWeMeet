package com.whenwemeet.availability.controller;

import com.whenwemeet.availability.dto.CreateAvailableDatesRequest;
import com.whenwemeet.availability.service.AvailableDateService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participants/{participantId}/available-dates")
public class AvailableDateController {

    private final AvailableDateService availableDateService;

    @PostMapping
    public Long saveAvailableDates(
            @PathVariable Long participantId,
            @RequestBody CreateAvailableDatesRequest request
    ) {
        return availableDateService.saveAvailableDates(participantId, request);
    }

    @GetMapping
    public List<LocalDate> getAvailableDates(
            @PathVariable Long participantId
    ) {
        return availableDateService.getAvailableDates(participantId);
    }
}