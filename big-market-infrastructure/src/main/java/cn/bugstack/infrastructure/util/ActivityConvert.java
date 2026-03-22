package cn.bugstack.infrastructure.util;

import cn.bugstack.domain.activity.model.entity.ActivityAccountEntity;
import cn.bugstack.domain.activity.model.entity.ActivityOrderEntity;
import cn.bugstack.domain.activity.model.entity.RaffleActivityOrderEntity;
import cn.bugstack.infrastructure.persistent.po.RaffleActivityAccount;
import cn.bugstack.infrastructure.persistent.po.RaffleActivityOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ActivityConvert {
    ActivityAccountEntity convertRaffleActivityAccount(RaffleActivityAccount raffleActivityAccount);

    ActivityOrderEntity RaffleActivityOrderEntityConvert(RaffleActivityOrder raffleActivityOrder);
}
