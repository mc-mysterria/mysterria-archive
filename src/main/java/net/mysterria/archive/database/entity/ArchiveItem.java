package net.mysterria.archive.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ArchiveItem {

    @Id
    @GeneratedValue(generator = "system-uuid")
    private Long id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_lore")
    private String itemLore;

    @Column(name = "item_thumbnail")
    private String itemThumbnail;

    @ManyToOne
    @JoinColumn(name = "player_found")
    private ArchivePlayer playerFound;

    @ManyToOne
    @JoinColumn(name = "item_type")
    private ArchiveType itemType;

    @ManyToOne
    @JoinColumn(name = "item_path")
    private ArchivePathway itemPath;
}
