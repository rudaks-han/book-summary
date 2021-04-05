package rudaks;

import java.time.Instant;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import rudaks.types.SimpleInvoice;

public class InvoiceTimeExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> consumerRecord, long prevTime) {
        SimpleInvoice invoice = (SimpleInvoice) consumerRecord.value();
        long eventTime = Instant.parse(invoice.getCreatedTime()).toEpochMilli();
        return ((eventTime>0) ? eventTime : prevTime);
    }
}
