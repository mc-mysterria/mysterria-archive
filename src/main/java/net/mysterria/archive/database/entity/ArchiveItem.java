package net.mysterria.archive.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "items")
public class ArchiveItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String purpose;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "researcher_id", nullable = false)
    private ArchiveResearcher archiveResearcher;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pathway_id")
    private ArchivePathway archivePathway;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ArchiveType archiveType;
    
    @Column(name = "sequence_number")
    private Integer sequenceNumber;
    
    @Column(name = "rarity")
    private String rarity;
    
    @OneToMany(mappedBy = "archiveItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchiveComment> archiveComments;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}