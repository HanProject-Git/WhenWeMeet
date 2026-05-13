package com.whenwemeet.room.service;

import com.whenwemeet.room.dto.CreateRoomRequest;
import com.whenwemeet.room.entity.Room;
import com.whenwemeet.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.whenwemeet.room.dto.RoomResponse;
import com.whenwemeet.room.dto.UpdateRoomRequest;
import org.springframework.transaction.annotation.Transactional;
import com.whenwemeet.member.entity.Member;
import com.whenwemeet.member.repository.MemberRepository;
import java.util.UUID;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    private final MemberRepository memberRepository;

    public Long createRoom(CreateRoomRequest request, Long memberId) {

        Member owner = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        String inviteCode = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        Room room = new Room(
                request.title(),
                request.description(),
                request.startDate(),
                request.endDate(),
                inviteCode,
                owner
        );

        Room savedRoom = roomRepository.save(room);

        return savedRoom.getId();
    }

    public RoomResponse getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("일정방을 찾을 수 없습니다."));

        if (room.isDeleted()) {
                throw new IllegalArgumentException("삭제된 방입니다.");
        }

        return RoomResponse.from(room);
    }

    public RoomResponse getRoomByInviteCode(String inviteCode) {
        Room room = roomRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("일정방을 찾을 수 없습니다."));

        if (room.isDeleted()) {
                throw new IllegalArgumentException("삭제된 방입니다.");
        }

        return RoomResponse.from(room);
    }

    @Transactional
    public RoomResponse updateRoom(Long roomId, UpdateRoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("일정방을 찾을 수 없습니다."));

        room.update(
                request.title(),
                request.description(),
                request.startDate(),
                request.endDate()
        );

        return RoomResponse.from(room);
    }

    @Transactional
    public void deleteRoom(Long roomId, Long memberId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("일정방을 찾을 수 없습니다."));

        if (room.isDeleted()) {
                throw new IllegalArgumentException("이미 삭제된 방입니다.");
        }

        if (!room.isOwner(memberId)) {
                throw new IllegalArgumentException("방장만 삭제할 수 있습니다.");
        }

        room.delete();
        }

    public List<RoomResponse> getOwnedRooms(Long memberId) {
        return roomRepository.findByOwnerIdAndDeletedFalse(memberId)
                .stream()
                .map(RoomResponse::from)
                .toList();
    }

    @Transactional
        public void confirmDate(
                Long roomId,
                Long memberId,
                LocalDate date
        ) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("일정방을 찾을 수 없습니다."));

        if (room.isDeleted()) {
                throw new IllegalArgumentException("삭제된 방입니다.");
        }

        if (!room.isOwner(memberId)) {
                throw new IllegalArgumentException("방장만 일정 확정이 가능합니다.");
        }

        if (date.isBefore(room.getStartDate())
                || date.isAfter(room.getEndDate())) {

                throw new IllegalArgumentException("방 기간 내 날짜만 확정 가능합니다.");
        }

        room.confirmDate(date);
        }
}
