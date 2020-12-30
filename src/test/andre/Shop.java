package test.andre;

import java.util.Random;

public class Shop {

    Random random = new Random();
    private String name;

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Shop(String name) {
        this.name = name;
    }
    
}
