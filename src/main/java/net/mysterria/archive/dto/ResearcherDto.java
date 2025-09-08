package net.mysterria.archive.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResearcherDto {
    private Long id;
    private String nickname;
    private LocalDateTime createdAt;
}