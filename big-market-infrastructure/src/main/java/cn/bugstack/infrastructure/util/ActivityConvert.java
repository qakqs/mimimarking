package cn.bugstack.infrastructure.util;

import cn.bugstack.domain.activity.model.entity.ActivityAccountEntity;
import cn.bugstack.infrastructure.persistent.po.RaffleActivityAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ActivityConvert {
    ActivityAccountEntity convertRaffleActivityAccount(RaffleActivityAccount raffleActivityAccount);

}
