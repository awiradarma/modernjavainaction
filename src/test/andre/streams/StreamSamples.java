package test.andre.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

public class StreamSamples {

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
        new Transaction(brian, 2011, 300),
        new Transaction(raoul, 2012, 1000),
        new Transaction(raoul, 2011, 400),
        new Transaction(mario, 2012, 710),
        new Transaction(mario, 2012, 700),
        new Transaction(alan, 2012, 950)
        );

        // 1. Find all transactions in the year 2011 and sort them by value (small to high).

        transactions.stream().filter(t -> t.getYear() == 2011).sorted(comparing(Transaction::getValue)).forEach(t -> System.out.println(t.getValue() + " " + t.getYear()));
   
        // 2. What are all the unique cities where the traders work?

        transactions.stream().map(t -> t.getTrader().getCity()).distinct().forEach(c -> System.out.println(c));
    
        // 3. Find all traders from Cambridge and sort them by name.

        transactions.stream().map(t -> t.getTrader()).filter(t -> t.getCity() == "Cambridge").map(t -> t.getName()).distinct().sorted().forEach(c -> System.out.println(c));

        //4. Return a string of all traders’ names sorted alphabetically.

        Optional <String> names = transactions.stream().map(t -> t.getTrader().getName()).sorted().distinct().reduce((a,b) -> a + ","+ b);
        names.ifPresent(x -> System.out.println(x));

        // 5. Are any traders based in Milan?

        boolean any_in_Milan = transactions.stream().anyMatch(t -> t.getTrader().getCity() == "Milan");
        System.out.println("Any traders based in Milan : " + any_in_Milan);

        // 6. Print the values of all transactions from the traders living in Cambridge.

        Integer cambridge_sum = transactions.stream().filter(t -> t.getTrader().getCity() == "Cambridge").map(Transaction::getValue).reduce(0, Integer::sum);
        System.out.println("Total values of all transactions from traders in Cambridge : " + cambridge_sum);

        // 7. What’s the highest value of all the transactions?

        Optional <Integer> highest = transactions.stream().map(t -> t.getValue()).reduce(Integer::max);
        highest.ifPresent(t -> System.out.println(t));

        // 8. Find the transaction with the smallest value.

        Optional<Transaction> smallest = transactions.stream().reduce((a, b) ->  a.getValue() < b.getValue() ? a : b);
        smallest.ifPresent(t -> System.out.println(t.getTrader().getName() + "," + t.getValue()));

    }
    
}

class Trader{
    private final String name;
    private final String city;
    public Trader(String n, String c){
        this.name = n;
        this.city = c;
    }
    public String getName(){
        return this.name;
    }
    public String getCity(){
        return this.city;
    }
    public String toString(){
        return "Trader:"+this.name + " in " + this.city;
    }
}

class Transaction{
    private final Trader trader;
    private final int year;
    private final int value;
    public Transaction(Trader trader, int year, int value){
        this.trader = trader;
        this.year = year;
        this.value = value;
    }
    public Trader getTrader(){
        return this.trader;
    }
    public int getYear(){
        return this.year;
    }
    public int getValue(){
        return this.value;
    }
    public String toString(){
        return "{" + this.trader + ", " +
               "year: "+this.year+", " +
               "value:" + this.value +"}";
    }
}
