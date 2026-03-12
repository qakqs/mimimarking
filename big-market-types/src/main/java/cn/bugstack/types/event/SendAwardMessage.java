package cn.bugstack.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendAwardMessage {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 奖品ID
     */
    private Integer awardId;
    /**
     * 奖品标题（名称）
     */
    private String awardTitle;
}
