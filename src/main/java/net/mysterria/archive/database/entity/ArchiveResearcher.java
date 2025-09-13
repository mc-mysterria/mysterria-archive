package net.mysterria.archive.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "researchers")
public class ArchiveResearcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(name = "backend_user_id", unique = true)
    private UUID backendUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "archiveResearcher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchiveItem> discoveredArchiveItems;

    @OneToMany(mappedBy = "archiveResearcher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchiveComment> archiveComments;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}