package com.ajbxyyx.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class MsgHistoryCursorQueryDTO extends BaseCursorQueryDTO{
    private Long chatId;

    public MsgHistoryCursorQueryDTO(Long chatId){
        this.chatId = chatId;
    }
}
