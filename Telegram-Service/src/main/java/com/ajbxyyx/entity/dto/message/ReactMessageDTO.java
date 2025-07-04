package com.ajbxyyx.entity.dto.message;

import lombok.Data;

@Data
public class ReactMessageDTO {
    private Long chatId;
    private Long msgId;
    private Integer reactType;
}
