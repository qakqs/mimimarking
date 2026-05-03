package cn.bugstack.domain.admin.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中奖统计投影
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardStatProjection {

    private Integer awardId;
    private String awardTitle;
    private Long awardCount;

}
