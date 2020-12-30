package test.andre.streams;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import test.andre.Shop;

public class CompletableFutureExercise {
    
    static List<Shop> shops = List.of(new Shop("BestPrice"), new Shop("LetsSaveBig"), new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"), new Shop("TheOnlyShopThatMatters"), new Shop("MyNeigborhoodShop"), new Shop("TheSeventh"), new Shop("EightIsRight"), new Shop("NineIsMine"));

    public static List<String> findPricesStream(String product) {
        return shops.stream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    public static List<String> findPricesParallelStream(String product) {
        return shops.parallelStream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    public static List<String> findPricesCompletableFuture(String product) {

            List<CompletableFuture<String>> priceFutures =
                    shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(
                                 () -> shop.getName() + " price is " +
                                       shop.getPrice(product)))
                    .collect(toList());
            return priceFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(toList());        
    
    }

    public static List<String> findPricesCompletableFutureCustomExecutor(String product) {

        final Executor executor =
        Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                                     (Runnable r) -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        );

            List<CompletableFuture<String>> priceFutures =
                    shops.stream()
                    .map(shop -> CompletableFuture.supplyAsync(
                                 () -> shop.getName() + " price is " +
                                       shop.getPrice(product),executor))
                    .collect(toList());
            return priceFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(toList());        
    
    }

    static void executeSearch(String product, Function<String,List<String>> f) {
        long start = System.nanoTime();
        System.out.println(f.apply(product));
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + invocationTime
                                                + " msecs");

    }
    public static void main(String[] args) {
        System.out.println("Search using stream serially");
        executeSearch("socks",CompletableFutureExercise::findPricesStream);

        System.out.println("Search using parallel stream");
        executeSearch("socks",CompletableFutureExercise::findPricesParallelStream);

        System.out.println("Search using CompletableFuture and default executor thread pool");
        executeSearch("socks",CompletableFutureExercise::findPricesCompletableFuture);

        System.out.println("Search using CompletableFuture and custom executor thread pool");
        executeSearch("socks",CompletableFutureExercise::findPricesCompletableFutureCustomExecutor);
    }
    
}
