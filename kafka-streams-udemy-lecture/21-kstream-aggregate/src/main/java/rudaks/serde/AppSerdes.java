package rudaks.serde;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import rudaks.types.DepartmentAggregate;
import rudaks.types.Employee;

public class AppSerdes extends Serdes {

    static final class EmployeeSerde extends WrapperSerde<Employee> {
        EmployeeSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<Employee> Employee() {
        EmployeeSerde serde = new EmployeeSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, Employee.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

    static final class DepartmentAggSerde extends WrapperSerde<DepartmentAggregate> {
        DepartmentAggSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<DepartmentAggregate> DepartmentAggregate() {
        DepartmentAggSerde serde = new DepartmentAggSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, DepartmentAggregate.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }
}
