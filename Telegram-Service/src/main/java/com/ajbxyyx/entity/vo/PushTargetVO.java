package com.ajbxyyx.entity.vo;

import com.ajbxyyx.entity.enums.PushTargetTypeEnum;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class PushTargetVO {

    private Long uid;

    private Integer targetType;
    private List<Long> targetUids;
    private Long targetGroupId;


    public static PushTargetVO buildPushTargetVO(Long groupId){
        PushTargetVO vo = new PushTargetVO();
        vo.setTargetType(PushTargetTypeEnum.GROUP.getType());
        vo.setTargetGroupId(groupId);
        return vo;
    }

    public static PushTargetVO buildPushTargetVO(List<Long> uids){
        PushTargetVO vo = new PushTargetVO();
        vo.setTargetType(PushTargetTypeEnum.PERSON.getType());
        vo.setTargetUids(uids);
        return vo;
    }


    public Boolean pushGroup(){
        if(PushTargetTypeEnum.of(targetType) == PushTargetTypeEnum.GROUP){
            return true;
        }else{
            return false;
        }
    }

}
