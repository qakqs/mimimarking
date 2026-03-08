package cn.bugstack.domain.strategy.service;

import cn.bugstack.types.entity.RaffleAwardEntity;
import cn.bugstack.types.entity.RaffleFactorEntity;

public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
