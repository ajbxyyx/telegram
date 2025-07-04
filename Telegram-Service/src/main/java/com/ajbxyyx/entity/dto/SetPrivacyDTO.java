package com.ajbxyyx.entity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetPrivacyDTO {
    @NotNull(message = "privacy is required")
    @Max(value= 9,message = "privacy max is 9")
    @Min(value= 1,message = "privacy min is 1")
    Integer privacy;
    @NotNull(message = "privacy is required")
    @Max(value= 3,message = "val max is 3")
    @Min(value= 1,message = "val min is 1")
    Integer val;
}
