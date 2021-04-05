package rudaks;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rudaks.serde.AppSerdes;
import rudaks.types.PosInvoice;

public class RewardsApp {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, PosInvoice> KS0 = builder.stream(
            AppConfigs.posTopicName, Consumed.with(AppSerdes.String(), AppSerdes.PosInvoice())
        ).filter((key, value) -> value.getCustomerType().equalsIgnoreCase(AppConfigs.CUSTOMER_TYPE_PRIME));

        StoreBuilder kvStoreBuilder = Stores.keyValueStoreBuilder(
            Stores.inMemoryKeyValueStore(AppConfigs.REWARD_STATE_NAME),
            AppSerdes.String(), AppSerdes.Double()
        );

        builder.addStateStore(kvStoreBuilder);

        KS0.transformValues(() -> new RewardsTransformer(), AppConfigs.REWARD_STATE_NAME)
            .to(AppConfigs.notificationTopic, Produced.with(AppSerdes.String(), AppSerdes.Notification()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}
