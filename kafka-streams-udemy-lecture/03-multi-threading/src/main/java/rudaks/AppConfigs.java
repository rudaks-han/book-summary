package rudaks;

class AppConfigs {
    final static String applicationID = "Multi-Threaded-Producer";
    final static String topicName = "nse-eod-topic";
    final static String bootstrapServers = "localhost:9092";
    final static String kafkaConfigFileLocation = "/Users/rudaks/_WORK/_GIT/javastudy/kafka-streams-udemy/kafka.properties";
    final static String[] eventFiles = {"data/NSE05NOV2018BHAV.csv","data/NSE06NOV2018BHAV.csv"};
}
