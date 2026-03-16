package cn.bugstack.domain.rebate.model.entity;

import cn.bugstack.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 行为类型枚举值
     */
    private BehaviorTypeVO  behaviorTypeVO;

    /**
     * 外部幂等
     */
    private String outBusinessNo;
}
