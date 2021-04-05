package rudaks;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rudaks.serde.AppSerdes;
import rudaks.types.PosInvoice;

public class PosFanoutApp {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, PosInvoice> KS0 = builder.stream(AppConfigs.posTopicName, Consumed.with(AppSerdes.String(), AppSerdes.PosInvoice()));

        KS0.filter((k, v) -> v.getDeliveryType().equalsIgnoreCase(AppConfigs.DELIVERY_TYPE_HOME_DELIVERY))
            .to(AppConfigs.shipmentTopicName, Produced.with(AppSerdes.String(), AppSerdes.PosInvoice()));

        KS0.filter((k, v) -> v.getCustomerType().equalsIgnoreCase(AppConfigs.CUSTOMER_TYPE_PRIME))
            .mapValues(invoice -> RecordBuilder.getNotification(invoice))
            .to(AppConfigs.notificationTopic, Produced.with(AppSerdes.String(), AppSerdes.Notification()));

        KS0.mapValues(invoice -> RecordBuilder.getMaskedInvoice(invoice))
            .flatMapValues(invoice -> RecordBuilder.getHadoopRecords(invoice))
            .to(AppConfigs.hadoopTopic, Produced.with(AppSerdes.String(), AppSerdes.HadoopRecord()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping streams");
            streams.close();
        }));

    }
}
