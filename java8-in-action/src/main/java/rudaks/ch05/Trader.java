package rudaks.ch05;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Trader {
    private final String name;

    private final String city;
}
