package test.andre.streams;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import static java.util.Comparator.comparing;

/**
 * First
 */
public class CustomFunctionalInterface {

    // Use method reference to the constructor
    static QuadFunction<String, Boolean, Integer, Dish.Type, Dish> DishFactory = Dish::new;

    static Predicate<Dish> vegetarian_food = Dish::isVegetarian;
    static Predicate<Dish> fish = (Dish d) -> d.getType() == Dish.Type.FISH;

    static Function <Dish, Integer> calories = Dish::getCalories;

    public static void main(String[] args) {
        List<Dish> menu = new ArrayList<>();
        menu.add(DishFactory.apply("pork", false, 800, Dish.Type.MEAT));
        menu.add(DishFactory.apply("beef", false, 700, Dish.Type.MEAT));
        menu.add(DishFactory.apply("chicken", false, 400, Dish.Type.MEAT));
        menu.add(DishFactory.apply("french fries", true, 530, Dish.Type.OTHER));
        menu.add(DishFactory.apply("rice", true, 350, Dish.Type.OTHER));
        menu.add(DishFactory.apply("season fruit", true, 120, Dish.Type.OTHER));
        menu.add(DishFactory.apply("pizza", true, 550, Dish.Type.OTHER));
        menu.add(DishFactory.apply("prawns", false, 300, Dish.Type.FISH));
        menu.add(new Dish("salmon", false, 450, Dish.Type.FISH));

        List<Dish> lowCalories = menu.stream().filter(fish).sorted(comparing(calories).reversed()).collect(toList());
        //map(Dish::getName).collect(toList());

        menu.stream().filter(d -> d.getType() == Dish.Type.MEAT).forEach(d -> System.out.println(d.getName()));
        for(Dish d : lowCalories) {
            System.out.println(d.getName() + " " + d.getCalories());
        }

        Optional<String> s = menu.stream().map(Dish::getName).reduce((a, b) -> a + "," + b);
        s.ifPresent(output -> System.out.println("All dishes: " + output));
        // System.out.println(lowCalories);
    }

    
}

@FunctionalInterface
interface QuadFunction<T, U, V, R, S> {
    S apply(T t, U u, V v, R r);
}

class Dish {
    public enum Type { MEAT, FISH, OTHER}

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;



    public Dish (String name, Boolean vegetarian, Integer calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian.booleanValue();
        this.calories = calories.intValue();
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    
}