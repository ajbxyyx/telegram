package com.ajbxyyx.entity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DeleteContactDTO {
    @NotNull(message = "id required")
    @Min(value = 1,message = "id must bigger than 0")
    private Long id;
}
