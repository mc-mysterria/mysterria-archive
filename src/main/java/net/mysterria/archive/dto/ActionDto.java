package net.mysterria.archive.dto;

import lombok.Data;
import net.mysterria.archive.enums.ActionType;

import java.time.LocalDateTime;

@Data
public class ActionDto {
    private Long id;
    private ActionType actionType;
    private LocalDateTime createdAt;
    private ResearcherDto researcher;
}