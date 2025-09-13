package net.mysterria.archive.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePathwayRequest {
    @NotBlank(message = "Pathway name cannot be blank")
    @Size(min = 2, max = 100, message = "Pathway name must be between 2 and 100 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Min(value = 1, message = "Sequence count must be at least 1")
    @Max(value = 9, message = "Sequence count cannot exceed 9")
    private Integer sequenceCount;
}