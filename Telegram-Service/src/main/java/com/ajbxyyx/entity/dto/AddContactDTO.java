package com.ajbxyyx.entity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class AddContactDTO {
    @NotNull(message = "id required")
    @Min(value = 1,message = "id must bigger than 1")
    private Long id;
    @NotBlank(message = "first name required")
    @Length(max=50,message = "first name max length is 50")
    private String firstName;

    @Length(max=50,message = "last name max length is 50")
    private String lastName;
}
