package cn.bugstack.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributeAwardEntity {
    private String userId;
    private Integer awardId;
    private String orderId;
    private String awardConfig;
}
