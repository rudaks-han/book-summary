package _02_adapter._01;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Banner {

    private String string;

    public void showWithParen() {
        System.out.println("(" + string + ")");
    }

    public void showWithAster() {
        System.out.println("*" + string + "*");
    }
}
