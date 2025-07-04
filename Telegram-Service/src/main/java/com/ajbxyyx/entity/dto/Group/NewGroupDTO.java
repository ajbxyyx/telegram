package com.ajbxyyx.entity.dto.Group;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class NewGroupDTO {


    @NotBlank(message = "owner is required")
    private Long owner;

    @NotBlank(message = "name is required")
    @Length(max = 25,message = "name max length is 25")
    private String name;


    private List<Long> inviteUidList;

}
