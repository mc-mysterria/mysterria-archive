package net.mysterria.archive.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.mysterria.archive.enums.ActionType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_actions")
public class ArchiveAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "researcher")
    private ArchiveResearcher archiveResearcher;

    @Column(name = "action_type")
    private ActionType actionType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
