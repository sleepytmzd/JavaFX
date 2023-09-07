package util;

import java.util.ArrayList;
import java.util.List;
import server.Food;
import java.io.Serializable;

public class Order implements Serializable{
    public String customerName;
    public int restaurantId;
    public List<Food> foods;

    public Order(){
        restaurantId = 0;
        foods = new ArrayList<>();
    }

    public void addFood(Food food){
        foods.add(food);
    }
}
