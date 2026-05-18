package com.whenwemeet.participant.repository;

import com.whenwemeet.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    long countByRoomId(Long roomId);

    Optional<Participant> findByRoomIdAndMemberId(Long roomId, Long memberId);

    boolean existsByRoomIdAndMemberId(Long roomId, Long memberId);

    List<Participant> findByRoom_Id(Long roomId);

    List<Participant> findByMember_Id(Long memberId);

    List<Participant> findByRoomId(Long roomId);
}