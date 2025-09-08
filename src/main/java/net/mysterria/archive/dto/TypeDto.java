package net.mysterria.archive.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TypeDto {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private LocalDateTime createdAt;
}