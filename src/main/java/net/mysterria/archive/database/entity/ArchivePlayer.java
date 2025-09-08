package net.mysterria.archive.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ArchivePlayer {

    @Id
    private String playerUUID;

    @Column(name = "player_name")
    private String playerName;
}
