package sample;

import java.io.Serializable;
public class Data implements Serializable{
    private String name;
    private String category;
    private String price;
    private String orders;
    public Data(String name, String category, Double price){
        this.name = name;
        this.category = category;
        this.price = String.valueOf(price);
        this.orders = "0";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setPrice(Double price) {
        this.price = String.valueOf(price);
    }

    public String getPrice() {
        return price;
    }

    public String getOrders() {
        return orders;
    }

    public void incrementOrders(){
        int n = Integer.parseInt(orders);
        n++;
        orders = String.valueOf(n);
    }

    public void incrementOrders(int amount) {
        int n = Integer.parseInt(orders);
        n = n + amount;
        orders = String.valueOf(n);
    }
}
