package com.whenwemeet.room.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateRoomRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 30, message = "방 제목은 30자 이하만 가능합니다.")
        String title,

        @Size(max = 200, message = "방 설명은 200자 이하만 가능합니다.")
        String description,

        @NotNull(message = "시작일은 필수입니다.")
        LocalDate startDate,

        @NotNull(message = "종료일은 필수입니다.")
        LocalDate endDate

) {
}