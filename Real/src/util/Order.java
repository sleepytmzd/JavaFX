package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import server.Food;
import java.io.Serializable;

public class Order implements Serializable{
    public String customerName;
    public int restaurantId;
    public List<Food> foods;
    public List<Integer> count;
    public Date date;

    public Order(){
        restaurantId = 0;
        foods = new ArrayList<>();
        count = new ArrayList<>();
    }

    public void addFood(Food food){
        foods.add(food);
    }
    /*public void clear(){
        customerName = "";
        restaurantId = 0;
        foods.clear();
        count.clear();
    }*/
}
