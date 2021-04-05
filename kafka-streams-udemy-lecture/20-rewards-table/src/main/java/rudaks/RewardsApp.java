package rudaks;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rudaks.serde.AppSerdes;
import rudaks.types.Notification;

public class RewardsApp {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Notification> KS0 = builder.stream(
            AppConfigs.posTopicName,
            Consumed.with(AppSerdes.String(), AppSerdes.PosInvoice()))
            .filter((key, value) -> value.getCustomerType().equalsIgnoreCase(AppConfigs.CUSTOMER_TYPE_PRIME))
            .map((key, invoice) -> new KeyValue<>(
                invoice.getCustomerCardNo(),
                Notifications.getNotificationFrom(invoice)
            ));

        KGroupedStream<String, Notification> KGS0 = KS0.groupByKey(Grouped.with(AppSerdes.String(), AppSerdes.Notification()));

        KTable<String, Notification> KT0 = KGS0.reduce((aggValue, newValue) -> {
            newValue.setTotalLoyaltyPoints(newValue.getEarnedLoyaltyPoints() + aggValue.getTotalLoyaltyPoints());
            return newValue;
        });

        KT0.toStream().to(AppConfigs.notificationTopic, Produced.with(AppSerdes.String(), AppSerdes.Notification()));

        logger.info("Starting Stream");
        KafkaStreams stream = new KafkaStreams(builder.build(), props);
        stream.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping Streams");
            stream.cleanUp();
        }));
    }
}
