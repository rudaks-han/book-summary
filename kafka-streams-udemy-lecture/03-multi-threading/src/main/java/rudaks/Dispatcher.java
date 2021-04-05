package rudaks;

import java.io.File;
import java.util.Scanner;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dispatcher implements Runnable {

    private static final Logger logger = LogManager.getLogger();
    private String fileLocation;

    private String topicName;

    private KafkaProducer<Integer, String> producer;

    Dispatcher(KafkaProducer<Integer, String> producer, String topicName, String fileLocation) {
        this.producer = producer;
        this.topicName = topicName;
        this.fileLocation = fileLocation;
    }

    @Override
    public void run() {
        logger.info("start processing " + fileLocation);
        File file = new File(fileLocation);
        int counter = 0;

        try(Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                producer.send(new ProducerRecord<>(topicName, null, line));
                logger.info("send..");
                counter++;
            }

            logger.info("Finished sending " + counter + " messages from " + fileLocation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

    }
}
