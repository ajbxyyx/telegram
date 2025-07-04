package com.ajbxyyx.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogInDTO {

    @NotBlank(message = "mobile country code is required")
    private String mobileCountry;

    @NotBlank(message = "mobile number is required")
    private String mobile;

    @NotNull(message = "keep signed in is required")
    private Boolean keepSignIn;




    private String botCheckCode;

}
