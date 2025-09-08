package net.mysterria.archive.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private String purpose;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ResearcherDto researcher;
    private List<CommentDto> comments;
}