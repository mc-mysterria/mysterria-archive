package net.mysterria.archive.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ArchivePathway {
    @Id
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "level")
    private int level;
}
