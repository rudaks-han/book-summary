package rudaks.ch11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ShopTest {

    private static List<Shop> shops = Arrays.asList(
        new Shop("BestPrice"),
        new Shop("LetsSaveBig"),
        new Shop("MyFavoriteShop"),
        new Shop("BuyItAll")
    );

    public static void main(String[] args) {

        long start = System.nanoTime();
        System.out.println(findPrices("myPhone275"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    public static List<String> findPrices(String product) {
        //return shops.stream().map(shop -> shop.getPrice(product)+"").collect(Collectors.toList());
        //return shops.parallelStream().map(shop -> shop.getPrice(product)+"").collect(Collectors.toList());
        //return shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> "" + shop.getPrice(product))).collect(Collectors.toList());

        List<CompletableFuture<String>> priceFutures = shops.stream()
            .map(shop -> CompletableFuture.supplyAsync(() -> "" + shop.getPrice(product))).collect(Collectors.toList());

        return priceFutures.stream().map(CompletableFuture::join)
            .collect(Collectors.toList());
    }
}
