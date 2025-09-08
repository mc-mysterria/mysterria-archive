package net.mysterria.archive.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private ResearcherDto researcher;
}