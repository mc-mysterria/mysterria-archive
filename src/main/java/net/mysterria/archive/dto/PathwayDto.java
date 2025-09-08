package net.mysterria.archive.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PathwayDto {
    private Long id;
    private String name;
    private String description;
    private Integer sequenceCount;
    private LocalDateTime createdAt;
}