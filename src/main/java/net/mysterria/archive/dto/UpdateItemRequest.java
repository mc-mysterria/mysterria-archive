package net.mysterria.archive.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateItemRequest {
    @Size(min = 2, max = 100, message = "Item name must be between 2 and 100 characters")
    private String name;
    
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
    
    @Size(max = 2000, message = "Purpose cannot exceed 2000 characters")
    private String purpose;
}