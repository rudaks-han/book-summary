package rudaks.serde;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import rudaks.types.SimpleInvoice;

public class AppSerdes extends Serdes {


    static final class PosInvoiceSerde extends WrapperSerde<SimpleInvoice> {
        PosInvoiceSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<SimpleInvoice> SimpleInvoice() {
        PosInvoiceSerde serde = new PosInvoiceSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, SimpleInvoice.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

}
