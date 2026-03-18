package cn.bugstack.domain.award.service;

import cn.bugstack.domain.award.model.entity.DistributeAwardEntity;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;

public interface IAwardService {
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    void distributeAward(DistributeAwardEntity  distributeAwardEntity);

}
