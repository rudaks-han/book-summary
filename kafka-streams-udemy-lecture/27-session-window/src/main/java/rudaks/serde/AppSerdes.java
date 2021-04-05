package rudaks.serde;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import rudaks.types.UserClicks;

public class AppSerdes extends Serdes {

    static final class PosInvoiceSerde extends WrapperSerde<UserClicks> {
        PosInvoiceSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<UserClicks> UserClicks() {
        PosInvoiceSerde serde = new PosInvoiceSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, UserClicks.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

}
