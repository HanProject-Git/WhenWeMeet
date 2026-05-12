package com.whenwemeet.participant.entity;

import com.whenwemeet.member.entity.Member;
import com.whenwemeet.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Participant(
            String name,
            Room room,
            Member member
    ) {
        this.name = name;
        this.room = room;
        this.member = member;
    }
}