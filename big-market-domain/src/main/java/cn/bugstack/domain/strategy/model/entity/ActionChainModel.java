package cn.bugstack.domain.strategy.model.entity;

import cn.bugstack.domain.activity.model.entity.ActivityCountEntity;
import cn.bugstack.domain.activity.model.entity.ActivitySkuEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ActionChainModel {

    ActivityEntity activityEntity;
    ActivitySkuEntity activitySkuEntity;
    ActivityCountEntity activityCountEntity;
}
