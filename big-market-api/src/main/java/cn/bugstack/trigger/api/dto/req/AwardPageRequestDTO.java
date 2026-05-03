package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 奖品分页查询请求 DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AwardPageRequestDTO extends PageRequestDTO {

    /** 奖品描述（模糊搜索） */
    private String awardDesc;

}
