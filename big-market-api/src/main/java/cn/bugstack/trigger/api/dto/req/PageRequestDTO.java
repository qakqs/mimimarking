package cn.bugstack.trigger.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 分页请求基类
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    /** 页码 */
    private Integer pageNum;

    /** 每页数量 */
    private Integer pageSize;

}
