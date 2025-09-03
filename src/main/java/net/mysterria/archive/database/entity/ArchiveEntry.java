package net.mysterria.archive.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArchiveEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private ArchiveType type;

    @Column(name = "player")
    private String player;

    @Column(name = "thumbnail")
    private String thumbnail;

}
