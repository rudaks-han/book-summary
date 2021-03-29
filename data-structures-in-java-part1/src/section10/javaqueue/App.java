package section10.javaqueue;

import java.util.LinkedList;
import java.util.Queue;

public class App {
    public static void main(String[] args) {
        Queue<Task> queue = new LinkedList<>();
        queue.add(new Task(1));
        queue.add(new Task(2));
        queue.add(new Task(3));

        while (!queue.isEmpty()) {
            Task task = queue.remove();
            task.execute();
        }
    }
}
