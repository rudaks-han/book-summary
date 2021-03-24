package rudaks.ch11;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {
    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);
        }).start();

        return futurePrice;
    }

    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public double calculatePrice(String product) {
        delay();

        Random random = new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public static void main(String[] args) {
        Shop shop = new Shop();

        /*long start = System.nanoTime();
        double result = shop.getPrice("car");
        long end = System.nanoTime() - start;

        System.out.println(result + ": " + end);*/

        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start));
        System.out.println(invocationTime);

        try {
            double price = futurePrice.get();
            System.out.printf("price is : %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("price returned after " + retrievalTime + "msecs");
    }
}
