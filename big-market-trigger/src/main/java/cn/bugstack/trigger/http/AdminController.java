package cn.bugstack.trigger.http;

import cn.bugstack.domain.admin.model.entity.AdminActivityEntity;
import cn.bugstack.domain.admin.model.entity.AdminStrategyEntity;
import cn.bugstack.domain.admin.service.IAdminActivityService;
import cn.bugstack.domain.admin.service.IAdminStrategyService;
import cn.bugstack.domain.strategy.model.valobj.LogicChainEnum;
import cn.bugstack.domain.strategy.model.valobj.RuleTreeNodeEnum;
import cn.bugstack.trigger.api.dto.req.CreateNewActivityReq;
import jakarta.annotation.Resource;
import cn.bugstack.types.common.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private static final Log log = Log.get(AdminController.class);

    @Resource
    IAdminActivityService adminActivityService;

    @Resource
    IAdminStrategyService adminStrategyService;


    /**
     * 获取抽奖策略
     *
     * @return
     */
    @RequestMapping(value = "getLogicChainModel", method = RequestMethod.POST)
    public Map<String, String> getLogicChainModel() {
        return LogicChainEnum.chainNameMap;
    }

    /**
     * 生成ActivityId(活动id)
     *
     * @return
     */
    @RequestMapping(value = "generateActivityId", method = RequestMethod.POST)
    public Integer generateActivityId() {
        return  adminActivityService.generateActivityId();
    }

    /**
     * 生成策略strategyId(策略id)
     *
     * @return
     */
    @RequestMapping(value = "generateStrategyId", method = RequestMethod.POST)
    public Integer generateStrategyId() {
        return adminStrategyService.generateStrategyId();

    }

    /**
     * 获取扣减策略
     *
     * @return
     */

    @RequestMapping(value = "getLogicCTreeModel", method = RequestMethod.POST)
    public Map<String, String> getLogicCTreeModel() {
        return RuleTreeNodeEnum.nodeNameMap;
    }

    /*
    创建活动
     */
    @RequestMapping(value = "createNewActivity", method = RequestMethod.POST)
    public void createNewActivity(@RequestBody CreateNewActivityReq request) {
        //1 创建抽奖活动
        AdminActivityEntity adminActivity = new AdminActivityEntity();
        adminActivityService.createActivity(adminActivity);
        //2 更新/新建相关抽奖策略(抽奖策略可能会提前于活动创建)
        AdminStrategyEntity strategyEntity = new AdminStrategyEntity();
        adminStrategyService.createStrategy(strategyEntity);
        //3 更新/新建活动sku(活动sku可能会提前于活动创建)
    }

    /*
    创建策略(抽奖策略,抽奖奖品概率,抽奖策略规则)
     */
    @RequestMapping(value = "createStrategy", method = RequestMethod.POST)
    public void createStrategy() {
        //

    }

    /*
    修改策略
     */
    @RequestMapping(value = "'editStrategy", method = RequestMethod.POST)
    public void editStrategy() {

    }


    /*
    修改规则树
     */
    @RequestMapping(value = "editRuleTree", method = RequestMethod.POST)
    public void editRuleTree() {

    }


}
