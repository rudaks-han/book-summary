package rudaks.ch02.example6;

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
