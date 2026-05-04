package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class WorkbenchSumResponse {

    /**
     * 活动总数
     */
    private Integer activitySum;

    /**
     * 用户总数
     */
    private Integer UserSum;
}
