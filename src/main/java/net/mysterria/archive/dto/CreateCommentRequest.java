package net.mysterria.archive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotBlank(message = "Comment content cannot be blank")
    @Size(max = 2000, message = "Comment content cannot exceed 2000 characters")
    private String content;
    
    @NotNull(message = "Item ID cannot be null")
    private Long itemId;
    
    @NotNull(message = "Researcher ID cannot be null")
    private Long researcherId;
}