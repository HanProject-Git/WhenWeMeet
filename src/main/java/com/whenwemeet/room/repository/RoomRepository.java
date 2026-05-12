package com.whenwemeet.room.repository;

import com.whenwemeet.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByInviteCode(String inviteCode);

    boolean existsByInviteCode(String inviteCode);

    List<Room> findByOwnerId(Long ownerId);
}