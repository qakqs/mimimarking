package cn.bugstack.trigger.http;

import cn.bugstack.domain.activity.model.entity.ActivityAccountEntity;
import cn.bugstack.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.bugstack.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.bugstack.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.bugstack.domain.activity.service.IRaffleActivityPartakeService;
import cn.bugstack.domain.activity.service.armory.IActivityArmory;
import cn.bugstack.domain.award.model.entity.UserAwardRecordEntity;
import cn.bugstack.domain.award.model.valobj.AwardStateVO;
import cn.bugstack.domain.award.service.IAwardService;
import cn.bugstack.domain.rebate.model.entity.BehaviorEntity;
import cn.bugstack.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.bugstack.domain.rebate.model.valobj.BehaviorTypeVO;
import cn.bugstack.domain.rebate.service.IBehaviorRebateService;
import cn.bugstack.domain.rebate.service.impl.BehaviorRebateService;
import cn.bugstack.domain.strategy.model.entity.RaffleAwardEntity;
import cn.bugstack.domain.strategy.model.entity.RaffleFactorEntity;
import cn.bugstack.domain.strategy.service.IRaffleStrategy;
import cn.bugstack.domain.strategy.service.armory.IStrategyArmory;
import cn.bugstack.trigger.api.IRaffleActivityService;
import cn.bugstack.trigger.api.dto.req.ActivityDrawRequestDTO;
import cn.bugstack.trigger.api.dto.req.UserActivityAccountRequestDTO;
import cn.bugstack.trigger.api.dto.resp.ActivityDrawResponseDTO;
import cn.bugstack.trigger.api.dto.resp.Response;
import cn.bugstack.trigger.api.dto.resp.UserActivityAccountResponseDTO;
import cn.bugstack.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static cn.bugstack.types.common.ResponseCode.*;

@Slf4j
@RestController
@RequestMapping("/api/raffle/activity")
public class RaffleActivityController implements IRaffleActivityService {
    @Resource
    private IActivityArmory activityArmory;

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Resource
    private IAwardService awardService;

    @Resource
    private IBehaviorRebateService behaviorRebateService;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    DateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> armory(Long activityId) {
        try {
            log.info("活动装配，数据预热，开始 activityId:{}", activityId);
            if (activityId == null) {
                throw new AppException(ILLEGAL_PARAMETER);
            }
            activityArmory.assembleActivitySkuByActivityId(activityId);
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);

            return Response.<Boolean>builder()
                    .code(SUCCESS.getCode())
                    .info(SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (AppException appException) {
            log.error("活动装配，数据预热，失败 activityId:{}", activityId, appException);
            return Response.<Boolean>builder()
                    .code(appException.getCode())
                    .info(appException.getInfo())
                    .data(false)
                    .build();

        } catch (Exception e) {
            log.error("活动装配，数据预热，失败 activityId:{}", activityId, e);
            return Response.<Boolean>builder()
                    .code(UN_ERROR.getCode())
                    .info(UN_ERROR.getInfo())
                    .data(false)
                    .build();

        }
    }

    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("活动抽奖，开始 request:{}", JSON.toJSONString(request));
            // 1 参数校验

            // 2 参与活动：创建参与活动订单&记录
            UserRaffleOrderEntity order = raffleActivityPartakeService.createOrder(PartakeRaffleActivityEntity
                    .builder()
                    .activityId(request.getActivityId())
                    .userId(request.getUserId())
                    .build()
            );
            // 3抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity
                    .builder()
                    .userId(order.getUserId())
                    .strategyId(order.getStrategyId())
                    .endDateTime(order.getEndDateTime())
                    .build()
            );
            // 4 存放结果
            UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder()
                    .userId(order.getUserId())
                    .activityId(order.getActivityId())
                    .strategyId(order.getStrategyId())
                    .orderId(order.getOrderId())
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .awardTime(new Date())
                    .awardState(AwardStateVO.create)
                    .awardConfig(raffleAwardEntity.getAwardConfig())
                    .build();
            awardService.saveUserAwardRecord(userAwardRecord);

            // 返回中将结果
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(SUCCESS.getCode())
                    .info(SUCCESS.getInfo())
                    .data(ActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .sort(raffleAwardEntity.getSort())
                            .build())
                    .build();
        } catch (AppException appException) {
            log.error("活动抽奖，抽奖失败 request:{}", JSON.toJSONString(request), appException);

            return Response.<ActivityDrawResponseDTO>builder()
                    .code(appException.getCode())
                    .info(appException.getInfo())
                    .build();

        } catch (Exception e) {
            log.error("活动抽奖，抽奖失败 request:{}", JSON.toJSONString(request), e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(UN_ERROR.getCode())
                    .info(UN_ERROR.getInfo())
                    .build();

        }
    }

    @RequestMapping(value = "calender_sign_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> calenderSignRebate(String userId) {
        try {
            log.info("日历签到返利 开始 userId:{}", userId);
            if (userId == null) {
                throw new AppException(ILLEGAL_PARAMETER);
            }
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
            behaviorEntity.setOutBusinessNo(dateFormatDay.format(new Date()));

            List<String> orderList = behaviorRebateService.createOrder(behaviorEntity);
            log.info("日历签到返利 结束 orderList:{}", orderList);

            return Response.<Boolean>builder()
                    .code(SUCCESS.getCode())
                    .info(SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (AppException appException) {
            log.error("日历签到返利 业务异常 ", appException);
            return Response.<Boolean>builder()
                    .code(appException.getCode())
                    .info(appException.getInfo())
                    .build();

        } catch (Exception e) {
            log.error("日历签到返利 系统异常 ", e);
            return Response.<Boolean>builder()
                    .code(UN_ERROR.getCode())
                    .info(UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "is_calender_sign_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> isCalenderSignRebate(String userId) {
        try {
            log.info("日历签到是否返利 开始 userId:{}", userId);
            String outBusinessNo = dateFormatDay.format(new Date());
            List<BehaviorRebateOrderEntity> orderByOutBusinessNo = behaviorRebateService.getOrderByOutBusinessNo(userId, outBusinessNo);
            log.info("日历签到是否返利 完成 userId:{}， orderSize:{}", userId, orderByOutBusinessNo.size());

            return Response.<Boolean>builder()
                    .code(SUCCESS.getCode())
                    .info(SUCCESS.getInfo())
                    .data(!orderByOutBusinessNo.isEmpty())
                    .build();

        } catch (AppException appException) {
            log.error("日历签到是否返利 业务异常 ", appException);
            return Response.<Boolean>builder()
                    .code(appException.getCode())
                    .info(appException.getInfo())
                    .build();

        } catch (Exception e) {
            log.error("日历签到是否返利 系统异常 ", e);
            return Response.<Boolean>builder()
                    .code(UN_ERROR.getCode())
                    .info(UN_ERROR.getInfo())
                    .build();

        }
    }

    @RequestMapping(value = "query_user_activity_account", method = RequestMethod.POST)
    @Override
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO request) {
        try {

            log.info("查询用户活动账户 开始 request:{}", request);
            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuotaService.queryActivityAccount(request.getUserId(), request.getActivityId());
            UserActivityAccountResponseDTO userActivityAccountResponseDTO = getUserActivityAccountResponseDTO(activityAccountEntity);

            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(SUCCESS.getCode())
                    .info(SUCCESS.getInfo())
                    .data(userActivityAccountResponseDTO)
                    .build();

        } catch (AppException appException) {
            log.error("日历签到是否返利 业务异常 ", appException);
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(appException.getCode())
                    .info(appException.getInfo())
                    .build();

        } catch (Exception e) {
            log.error("日历签到是否返利 系统异常 ", e);
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(UN_ERROR.getCode())
                    .info(UN_ERROR.getInfo())
                    .build();

        }
    }

    private static UserActivityAccountResponseDTO getUserActivityAccountResponseDTO(ActivityAccountEntity activityAccountEntity) {
        UserActivityAccountResponseDTO userActivityAccountResponseDTO = new UserActivityAccountResponseDTO();
        userActivityAccountResponseDTO.setTotalCount(activityAccountEntity.getTotalCount());
        userActivityAccountResponseDTO.setTotalCountSurplus(activityAccountEntity.getTotalCountSurplus());
        userActivityAccountResponseDTO.setDayCount(activityAccountEntity.getDayCount());
        userActivityAccountResponseDTO.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
        userActivityAccountResponseDTO.setMonthCount(activityAccountEntity.getMonthCount());
        userActivityAccountResponseDTO.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
        return userActivityAccountResponseDTO;
    }
}
