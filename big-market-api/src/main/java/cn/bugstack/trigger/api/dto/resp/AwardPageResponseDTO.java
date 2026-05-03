package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 奖品分页列表项响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardPageResponseDTO {

    /** 奖品ID */
    private Integer awardId;

    /** 奖品对接标识 */
    private String awardKey;

    /** 奖品配置信息 */
    private String awardConfig;

    /** 奖品内容描述 */
    private String awardDesc;

    /** 创建时间 */
    private String createTime;

}
