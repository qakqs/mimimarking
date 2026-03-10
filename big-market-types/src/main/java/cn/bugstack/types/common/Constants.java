package cn.bugstack.types.common;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";


    public static class RedisKey {
        public static final String RULE_TREE_VO_KEY = "rule_tree_vo_key_";
        public static final String STRATEGY_AWARD_LIST_KEY = "strategy_award_list_key";
        public static final String STRATEGY_KEY = "big_market_strategy_key_";
        public static final String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static final String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static final String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
        public static final String STRATEGY_AWARD_COUNT_KEY = "big_market_strategy_award_count_key_";
        public static final String STRATEGY_AWARD_COUNT_QUEUE_KEY = "big_market_strategy_award_query_key_";
        public static final String ACTIVITY_KEY = "big_market_activity_key_";
        public static final String ACTIVITY_COUNT_KEY = "big_market_activity_count_key_";
        public static final String ACTIVITY_SKU_STOCK_COUNT_KEY = "big_market_activity_sku_stock_count_key_";
        public static final String STRATEGY_SKU_COUNT_QUEUE_KEY = "big_market_sku_award_query_key_";
    }

    public static String RULE_TREE_VO_KEY(String treeId) {
        return Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
    }

    public static String STRATEGY_AWARD_LIST_KEY(Long strategyId) {
        return RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
    }

    public static String STRATEGY_KEY(Long strategyId) {
        return Constants.RedisKey.STRATEGY_KEY + strategyId;
    }

    public static String STRATEGY_AWARD_KEY(Long strategyId) {
        return Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
    }

    public static String STRATEGY_RATE_TABLE_KEY(String key) {
        return Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key;
    }

    public static String STRATEGY_RATE_RANGE_KEY(String key) {
        return Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key;
    }

    public static String STRATEGY_AWARD_COUNT_KEY(Long strategyId, Integer awardId) {
        return Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId +
                Constants.UNDERLINE + awardId;
    }

    public static String STRATEGY_AWARD_COUNT_QUEUE_KEY() {
        return Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
    }

    public static String ACTIVITY_KEY(Long activityId) {
        return Constants.RedisKey.ACTIVITY_KEY + activityId;
    }
    public static String ACTIVITY_COUNT_KEY(Long activityId) {
        return Constants.RedisKey.ACTIVITY_COUNT_KEY + activityId;
    }

    public static String STRATEGY_SKU_COUNT_QUEUE_KEY() {
        return Constants.RedisKey.STRATEGY_SKU_COUNT_QUEUE_KEY;
    }
    public static String ACTIVITY_SKU_STOCK_COUNT_KEY(long key) {
        return Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + key;
    }


}
