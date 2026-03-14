package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDrawResponseDTO {
    /**
     * 抽奖奖品ID - 内部流转使用
     */
    private Integer awardId;


    /**
     * 奖品标题（名称）
     */
    private String awardTitle;

    /**
     * 奖品顺序号
     */
    private Integer sort;
}
