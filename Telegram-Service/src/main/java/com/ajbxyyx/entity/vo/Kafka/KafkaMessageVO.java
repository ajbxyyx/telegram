package com.ajbxyyx.entity.vo.Kafka;

import com.ajbxyyx.utils.ThreadLocalUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageVO {
    private Long sender = ThreadLocalUtil.getUid();
    private Long receive;
    private String json;

    public KafkaMessageVO( Long receive, String json) {
        this.receive = receive;
        this.json = json;
    }
}
