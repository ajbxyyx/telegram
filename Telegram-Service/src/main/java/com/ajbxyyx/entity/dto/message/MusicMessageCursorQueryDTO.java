package com.ajbxyyx.entity.dto.message;

import com.ajbxyyx.entity.dto.BaseCursorQueryDTO;
import lombok.Data;

@Data
public class MusicMessageCursorQueryDTO extends BaseCursorQueryDTO {
    private Long chatId;
}
