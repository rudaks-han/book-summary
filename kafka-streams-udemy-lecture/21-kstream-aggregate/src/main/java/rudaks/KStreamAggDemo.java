package rudaks;

import java.util.Properties;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rudaks.serde.AppSerdes;
import rudaks.types.DepartmentAggregate;

public class KStreamAggDemo {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(StreamsConfig.STATE_DIR_CONFIG, AppConfigs.stateStoreLocation);

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder.stream(AppConfigs.topicName,
            Consumed.with(AppSerdes.String(), AppSerdes.Employee()))
            .groupBy((k, v) -> v.getDepartment(), Grouped.with(AppSerdes.String(), AppSerdes.Employee()))
            .aggregate(
                // initializer
                () -> new DepartmentAggregate()
                .withEmployeeCount(0)
                .withTotalSalary(0)
                    .withAvgSalary(0D),
                // aggregator
                (k, v, aggValue) -> new DepartmentAggregate()
                .withEmployeeCount(aggValue.getEmployeeCount() + 1)
                .withTotalSalary(aggValue.getTotalSalary() + v.getSalary())
                .withAvgSalary((aggValue.getAvgSalary() + v.getSalary()) / (aggValue.getEmployeeCount() + 1D)),
                //serializer
                Materialized.<String, DepartmentAggregate, KeyValueStore<Bytes, byte[]>> as(AppConfigs.stateStoreName)
                .withKeySerde(AppSerdes.String())
                .withValueSerde(AppSerdes.DepartmentAggregate())
            ).toStream().print(Printed.toSysOut().<String, DepartmentAggregate>toSysOut().withLabel("Department salary average"));


        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Stopping Streams");
            streams.close();
        }));

    }
}
