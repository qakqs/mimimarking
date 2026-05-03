package cn.bugstack.trigger.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应封装
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 总数 */
    private Long total;

    /** 数据列表 */
    private List<T> list;

}
