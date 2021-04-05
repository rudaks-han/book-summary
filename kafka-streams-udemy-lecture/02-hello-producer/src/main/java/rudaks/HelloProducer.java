package rudaks;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloProducer {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        // 1. configuration 설정
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 2. Producer object
        KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);

        // 3. send message
        for (int i=0; i<AppConfigs.numEvents; i++) {
            producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "Simple Message-" + i));
        }

        // 4. close producer
        producer.close();
    }
}
