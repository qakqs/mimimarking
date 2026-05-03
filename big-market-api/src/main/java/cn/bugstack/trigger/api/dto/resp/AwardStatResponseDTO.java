package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中奖统计响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardStatResponseDTO {

    /** 奖品ID */
    private Integer awardId;

    /** 奖品标题 */
    private String awardTitle;

    /** 中奖次数 */
    private Long awardCount;

}
