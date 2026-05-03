package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建/编辑奖品定义请求 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardCreateRequestDTO {

    /** 奖品ID（编辑时传入） */
    private Integer awardId;

    /** 奖品对接标识 */
    private String awardKey;

    /** 奖品配置信息 */
    private String awardConfig;

    /** 奖品内容描述 */
    private String awardDesc;

}
