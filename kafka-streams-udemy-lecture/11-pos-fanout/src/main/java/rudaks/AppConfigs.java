package rudaks;

public class AppConfigs {
    public final static String applicationID = "PosFanout";
    public final static String bootstrapServers = "localhost:9092";
    public final static String posTopicName = "pos";
    public final static String shipmentTopicName = "shipment";
    public final static String notificationTopic = "loyalty";
    public final static String hadoopTopic = "hadoop-sink";
    public final static String DELIVERY_TYPE_HOME_DELIVERY = "HOME-DELIVERY";
    public final static String CUSTOMER_TYPE_PRIME = "PRIME";
    public final static Double LOYALTY_FACTOR = 0.02;
}
