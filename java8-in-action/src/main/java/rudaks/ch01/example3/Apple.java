package rudaks.ch01.example3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Apple {

    private String color;

    private int weight;

    public Apple(String color) {
        this.color = color;
    }
}
