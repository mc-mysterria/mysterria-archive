package net.mysterria.archive.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateItemRequest {
    @NotBlank(message = "Item name cannot be blank")
    @Size(min = 2, max = 100, message = "Item name must be between 2 and 100 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 2000, message = "Purpose cannot exceed 2000 characters")
    private String purpose;

    private Long pathwayId;

    private Long typeId;

    @Min(value = 0, message = "Sequence number cannot be negative")
    @Max(value = 9, message = "Sequence number cannot exceed 9")
    private Integer sequenceNumber;

    @Pattern(regexp = "^(COMMON|UNCOMMON|RARE|EPIC|LEGENDARY|MYTHICAL)?$",
            message = "Rarity must be one of: COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHICAL")
    private String rarity;
}