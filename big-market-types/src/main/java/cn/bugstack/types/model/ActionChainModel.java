package cn.bugstack.types.model;

import cn.bugstack.types.entity.ActivityCountEntity;
import cn.bugstack.types.entity.ActivityEntity;
import cn.bugstack.types.entity.ActivitySkuEntity;
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
