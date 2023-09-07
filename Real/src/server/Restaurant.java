package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;
public class Restaurant implements  Serializable{

    public int id;
    public String name;
    public double score;
    public String price;
    public String zipcode;
    public String[] categories;
    public List<Food> foods;
    private static final String MENU_FILE = "menu.txt";

    public Restaurant(int id, String name, double score, String price, String zipcode, String[] categories){
        this.id = id;
        this.name = name;
        this.score = score;
        this.price = price;
        this.zipcode = zipcode;
        this.categories = categories;
        foods = new ArrayList<>();
        try{
            setFoods();
        }
        catch(Exception e) {
            System.out.println("Failed " + e);
        }

    }

    private void setFoods() throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(MENU_FILE));
        while(true) {
            String line = br.readLine();
            if (line == null) break;
            String[] array = line.split(",", -1);

            int restaurantId = Integer.parseInt(array[0]);
            String category = array[1];

            String name = array[2];
            if (array.length > 4) {
                for (int i = 3; i < array.length - 1; i++) {
                    name = name.concat("," + array[i]);
                }
            }

            double price = Double.parseDouble(array[array.length - 1]);

            if(restaurantId == id) {
                foods.add(new Food(restaurantId, category, name, price));
            }
        }
        br.close();
    }

    public List<Food> getFoods(){
        return foods;
    }

    public void addFood(Food food){
        foods.add(food);
    }

    @Override
    public String toString(){
        String ret = "Id: " + id + "\nName: " + name + "\nScore: " + score + "\nPrice: " + price + "\nZipcode: " + zipcode + "\nCategories: ";
        for(int i = 0; i < categories.length; i++){
            ret = ret + categories[i] + ", ";
        }
        return ret;
    }


}
