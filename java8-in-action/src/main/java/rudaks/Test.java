package rudaks;

import rudaks.ch08.Task;

public class Test {

    public static void something(Runnable r) {
        r.run();
    }

    public static void something(Task task) {
        task.execute();
    }

    public static void main(String[] args) {
        something((Task) () -> System.out.println("task"));
    }
}

