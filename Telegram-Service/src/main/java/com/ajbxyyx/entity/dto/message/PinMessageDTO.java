package com.ajbxyyx.entity.dto.message;

import lombok.Data;

@Data
public class PinMessageDTO {
    private Long msgId;
    private Long chatId;//unnecessary
    private Integer type;
}
