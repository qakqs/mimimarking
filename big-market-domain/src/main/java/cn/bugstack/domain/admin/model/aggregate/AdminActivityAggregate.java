package cn.bugstack.domain.admin.model.aggregate;

import cn.bugstack.domain.admin.model.entity.AdminActivityCountEntity;
import cn.bugstack.domain.admin.model.entity.AdminActivityEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台管理 - 活动聚合（活动 + 次数配置）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminActivityAggregate {

    private AdminActivityEntity activity;
    private AdminActivityCountEntity count;

}
