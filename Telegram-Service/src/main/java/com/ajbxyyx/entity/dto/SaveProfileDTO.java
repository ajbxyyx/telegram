package com.ajbxyyx.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SaveProfileDTO {

    @NotBlank(message = "first name is required.")
    @Length(max = 25,message = "first name max length is 25.")
    private String firstName;

    @Length(max = 25,message = "lasst name max length is 25.")
    private String lastName;

    @Length(max = 50,message = "bio max length is 50.")
    private String bio;

    @Length(max = 32,min = 5,message = "username length is between 5 and 32.")
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$",message = "username is unavailable")
    private String username;

}
