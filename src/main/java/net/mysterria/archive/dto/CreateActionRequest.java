package net.mysterria.archive.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.mysterria.archive.enums.ActionType;

@Data
public class CreateActionRequest {
    @NotNull(message = "Researcher ID cannot be null")
    private Long researcherId;

    @NotNull(message = "Action type cannot be null")
    private ActionType actionType;
}