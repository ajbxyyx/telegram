package com.ajbxyyx.entity.dto.Group;

import com.ajbxyyx.entity.dto.BaseCursorQueryDTO;
import lombok.Data;

@Data
public class GroupMemberCursorQueryDTO extends BaseCursorQueryDTO {

    private Long groupId;

}
