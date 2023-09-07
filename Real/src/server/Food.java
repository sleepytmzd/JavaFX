package server;

import java.io.Serializable;

public class Food implements  Serializable{

    public int restaurantId;
    public String category;
    public String name;
    public double price;

    public Food(int restaurantId, String category, String name, double price){
        this.restaurantId = restaurantId;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString(){
        return "Restaurant Id: " + restaurantId + ", Category: " + category + ", Name: " + name + ", Price: " + price;
    }

}
