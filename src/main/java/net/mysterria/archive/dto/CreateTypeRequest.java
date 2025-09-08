package net.mysterria.archive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTypeRequest {
    @NotBlank(message = "Type name cannot be blank")
    @Size(min = 2, max = 100, message = "Type name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @Size(max = 500, message = "Icon URL cannot exceed 500 characters")
    private String iconUrl;
}