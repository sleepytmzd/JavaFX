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

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return "Restaurant Id: " + restaurantId + ", Category: " + category + ", Name: " + name + ", Price: " + price;
    }

}
