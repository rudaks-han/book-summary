package _02_adapter._02;

import _02_adapter._01.Print;
import _02_adapter._01.PrintBanner;

public class Main {

    public static void main(String[] args) {
        Print p = new PrintBanner("Hello");
        p.printWeak();
        p.printStrong();
    }
}
