package net.mysterria.archive.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.mysterria.archive.enums.ActionType;

@Data
public class UpdateActionRequest {
    @NotNull(message = "Action type cannot be null")
    private ActionType actionType;
}