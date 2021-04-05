package rudaks;

public class AppConfigs {
    public final static String applicationID = "RewardsApp";
    public final static String bootstrapServers = "localhost:9092,localhost:9093";
    public final static String posTopicName = "pos";
    public final static String notificationTopic = "loyalty";
    public final static String CUSTOMER_TYPE_PRIME = "PRIME";
    public final static Double LOYALTY_FACTOR = 0.02;
    public final static String REWARDS_STORE_NAME = "CustomerRewardsStore";
    public final static String REWARDS_TEMP_TOPIC = "CustomerRewardsTempTopic";
}
