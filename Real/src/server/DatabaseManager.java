package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;

public class DatabaseManager implements Serializable{

    private List<Restaurant> restaurantList;
    private List<Food> foodList;

    private static final String RESTAURANT_FILE = "restaurant.txt";
    private static final String MENU_FILE = "menu.txt";
    //private static final String OUTPUT_RESTAURANT_FILE = "E:\\Porashuna\\1-2\\OOP dump\\Sessional\\Java_Term_Project\\Real\\src\\server\\restaurant.txt";
    //private  static  final String OUTPUT_MENU_FILE = "E:\\Porashuna\\1-2\\OOP dump\\Sessional\\Java_Term_Project\\Real\\src\\server\\menu.txt";

    public DatabaseManager(){
        restaurantList = new ArrayList<>();
        foodList = new ArrayList<>();

        try{
            loadFromRestaurantFile();
        }catch(Exception e){
            System.out.println("Cant load restaurant file " + e);
        }
        try{
            loadFromMenuFile();
        }catch(Exception e){
            System.out.println("Cant load from menu file " + e);
        }
    }

    public void addRestaurant(int id, String name, double score, String price, String zipcode, String[] categories){
        restaurantList.add(new Restaurant(id, name, score, price, zipcode, categories));
    }

    public void addFood(int restaurantId, String category, String name, double price){
        foodList.add(new Food(restaurantId, category, name, price));
        //restaurantList.get(restaurantId-1).addFood(new Food(restaurantId, category, name, price));
    }

    public void showRestaurantFoods(){
        for(int i = 0; i < restaurantList.size(); i++){
            List<Food> foods = restaurantList.get(i).foods;
            System.out.println("rest " + (i+1));
            for(int j = 0; j < foods.size(); j++){
                System.out.println(foods.get(j));
            }
        }
    }

    public Restaurant searchRestaurantByName(String name){
        for(int i = 0; i < restaurantList.size(); i++){
            //if(Objects.equals(restaurantList.get(i).name.toLowerCase(), name.toLowerCase())){
            if(restaurantList.get(i).name.toLowerCase().contains(name.toLowerCase())){
                return restaurantList.get(i);
            }
        }
        return null;
    }

    public List<Restaurant> searchRestaurantByScore(double lowerRange, double upperRange){
        List<Restaurant> foundRestaurants = new ArrayList<>();
        for(int i = 0; i < restaurantList.size(); i++){
            if(restaurantList.get(i).score >= lowerRange && restaurantList.get(i).score <= upperRange){
                foundRestaurants.add(restaurantList.get(i));
            }
        }
        return foundRestaurants;
    }

    public List<Restaurant> searchRestaurantByCategory(String category){
        List<Restaurant> foundRestaurants = new ArrayList<>();
        for(int i = 0; i < restaurantList.size(); i++){
            for(int j = 0; j < restaurantList.get(i).categories.length; j++){
                //if(Objects.equals(category.toLowerCase(), restaurantList.get(i).categories[j].toLowerCase())){
                if(restaurantList.get(i).categories[j].toLowerCase().contains(category.toLowerCase())){
                    foundRestaurants.add(restaurantList.get(i));
                    break;
                }
            }
        }
        return  foundRestaurants;
    }

    public List<Restaurant> searchRestaurantByPrice(String price){
        List<Restaurant> foundRestaurants = new ArrayList<>();
        for(int i = 0; i < restaurantList.size(); i++){
            if(Objects.equals(price, restaurantList.get(i).price)){
                foundRestaurants.add(restaurantList.get(i));
            }
        }
        return foundRestaurants;
    }

    public List<Restaurant> searchRestaurantByZipcode(String zipcode){
        List<Restaurant> foundRestaurants = new ArrayList<>();
        for(int i = 0; i < restaurantList.size(); i++){
            if(Objects.equals(zipcode, restaurantList.get(i).zipcode)){
                foundRestaurants.add(restaurantList.get(i));
            }
        }
        return foundRestaurants;
    }

    public List<String> getRestaurantCategoryList(){
        List<String> categoryList = new ArrayList<>();
        for(int k = 0; k < restaurantList.size(); k++) {
            for (int i = 0; i < restaurantList.get(k).categories.length; i++) {
                int found = 0;
                for (int j = 0; j < categoryList.size(); j++) {
                    if (Objects.equals(restaurantList.get(k).categories[i].toLowerCase(), categoryList.get(j).toLowerCase())) {
                        found = 1;
                        break;
                    }
                }
                if (found == 0) {
                    categoryList.add(restaurantList.get(k).categories[i]);
                }
            }
        }
        return categoryList;
    }

    public List<List<Restaurant>> getRestaurantsByCategory(List<String> categoryList){
        List<List<Restaurant>> restaurantsByCategory = new ArrayList<>(categoryList.size());
        for(int i = 0; i < categoryList.size(); i++){
            restaurantsByCategory.add(new ArrayList<>());
        }
        for(int i = 0; i < categoryList.size(); i++){
            for(int j = 0; j < restaurantList.size(); j++){
                for(int k = 0; k < restaurantList.get(j).categories.length; k++){
                    if(Objects.equals(restaurantList.get(j).categories[k].toLowerCase(), categoryList.get(i).toLowerCase())){
                        restaurantsByCategory.get(i).add(restaurantList.get(j));
                    }
                }
            }
        }
        return restaurantsByCategory;
    }

    public boolean foodIsAdded(int restaurantId, String category, String name){
        for(int i = 0; i < foodList.size(); i++){
            if(foodList.get(i).restaurantId == restaurantId && Objects.equals(foodList.get(i).category.toLowerCase(), category.toLowerCase()) && Objects.equals(foodList.get(i).name.toLowerCase(), name.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public List<Food> getFoodsInRestaurant(int id){
        List<Food> foundFoods = new ArrayList<>();
        for(int i = 0; i < foodList.size(); i++){
            if(id == foodList.get(i).restaurantId){
                foundFoods.add(foodList.get(i));
            }
        }
        return foundFoods;
    }

    public List<Food> searchFoodByName(String name){
        List<Food> foundFoods = new ArrayList<>();
        for(int i = 0; i < foodList.size(); i++){
            //if(Objects.equals(name.toLowerCase(), foodList.get(i).name.toLowerCase())){
            if(foodList.get(i).name.toLowerCase().contains(name.toLowerCase())){
                foundFoods.add(foodList.get(i));
            }
        }
        return foundFoods;
    }

    public List<Food> searchFoodByNameInRestaurant(String foodName, String restaurantName){
        List<Food> foundFoods = new ArrayList<>();
        Restaurant restaurant = searchRestaurantByName(restaurantName);
        if(restaurant == null){
            return null;
        }
        int id = restaurant.id;

        for(int i = 0; i < foodList.size(); i++){
            //if(id == foodList.get(i).restaurantId && Objects.equals(foodName.toLowerCase(), foodList.get(i).name.toLowerCase())){
            if(id == foodList.get(i).restaurantId && foodList.get(i).name.toLowerCase().contains(foodName.toLowerCase())){
                foundFoods.add(foodList.get(i));
            }
        }
        return foundFoods;
    }

    public List<Food> searchFoodByCategory(String category){
        List<Food> foundFoods = new ArrayList<>();
        for(int i = 0; i < foodList.size(); i++){
            //if(Objects.equals(category.toLowerCase(), foodList.get(i).category.toLowerCase())){
            if(foodList.get(i).category.toLowerCase().contains(category.toLowerCase())){
                foundFoods.add(foodList.get(i));
            }
        }
        return foundFoods;
    }

    public List<Food> searchFoodByCategoryInRestaurant(String category, String restaurantName){
        List<Food> foundFoods = new ArrayList<>();
        Restaurant restaurant = searchRestaurantByName(restaurantName);
        if(restaurant == null){
            return null;
        }
        int id = restaurant.id;

        for(int i = 0; i < foodList.size(); i++){
            //if(id == foodList.get(i).restaurantId && Objects.equals(category.toLowerCase(), foodList.get(i).category.toLowerCase())){
            if(id == foodList.get(i).restaurantId && foodList.get(i).category.toLowerCase().contains(category.toLowerCase())){
                foundFoods.add(foodList.get(i));
            }
        }
        return foundFoods;
    }

    public List<Food> searchFoodByPriceRange(double lowerRange, double upperRange){
        List<Food> foundFoods = new ArrayList<>();
        for(int i = 0; i < foodList.size(); i++){
            if(foodList.get(i).price >= lowerRange && foodList.get(i).price <= upperRange){
                foundFoods.add(foodList.get(i));
            }
        }
        return foundFoods;
    }

    public List<Food> searchFoodByPriceRangeInRestaurant(double lowerRange, double upperRange, String restaurantName){
        List<Food> foundFoods = new ArrayList<>();
        Restaurant restaurant = searchRestaurantByName(restaurantName);
        if(restaurant == null){
            return null;
        }
        int id = restaurant.id;

        for(int i = 0; i < foodList.size(); i++){
            if(id == foodList.get(i).restaurantId && foodList.get(i).price >= lowerRange && foodList.get(i).price <= upperRange){
                foundFoods.add(foodList.get(i));
            }
        }
        return foundFoods;
    }

    public List<Food> costliestFoodsInRestaurant(String restaurantName){
        List<Food> foundFoods = new ArrayList<>();
        Restaurant restaurant = searchRestaurantByName(restaurantName);
        if(restaurant == null){
            return null;
        }
        int id = restaurant.id;

        List<Food> eligibleFoodList = getFoodsInRestaurant(id);

        double maxPrice = -1;
        for(int i = 0; i < eligibleFoodList.size(); i++){
            if(maxPrice < eligibleFoodList.get(i).price){
                maxPrice = eligibleFoodList.get(i).price;
            }
        }
        for(int i = 0; i < eligibleFoodList.size(); i++){
            if(maxPrice == eligibleFoodList.get(i).price){
                foundFoods.add(eligibleFoodList.get(i));
            }
        }
        return  foundFoods;
    }

    public List<String> getRestaurantNameList(){
        List<String> nameList = new ArrayList<>();
        for(int i = 0; i < restaurantList.size(); i++){
            nameList.add(restaurantList.get(i).name);
        }
        return nameList;
    }

    public List<List<Food>> getFoodsByRestaurants(){
        List<List<Food>> foodsByRestaurants = new ArrayList<>(restaurantList.size());
        for(int i = 0; i < restaurantList.size(); i++){
            foodsByRestaurants.add(getFoodsInRestaurant(i+1));
        }
        return foodsByRestaurants;
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantList;
    }

    public  List<Food> getAllFoods(){
        return  foodList;
    }

    public int totalRestaurants(){
        return restaurantList.size();
    }

    public void loadFromRestaurantFile() throws  Exception{
        BufferedReader br = new BufferedReader(new FileReader(RESTAURANT_FILE));
        while(true){
            String line = br.readLine();
            if (line == null) break;
            String [] array = line.split(",", -1);

            int id = Integer.parseInt(array[0]);
            String name = array[1];
            double score = Double.parseDouble(array[2]);
            String price = array[3];
            String zipcode = array[4];
            String[] categories = new String[array.length - 5];
            for(int i = 5; i < array.length; i ++){
                categories[i - 5] = array[i];
            }

            addRestaurant(id, name, score, price, zipcode, categories);
        }
        //database.showAllRestaurants();
        //showAllRestaurants(database.getAllRestaurants());
        br.close();
    }

    public void loadFromMenuFile() throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(MENU_FILE));
        while(true){
            String line = br.readLine();
            if (line == null) break;
            String [] array = line.split(",", -1);

            int restaurantId = Integer.parseInt(array[0]);
            String category = array[1];

            String name = array[2];
            if(array.length > 4){
                for(int i = 3; i < array.length - 1; i++){
                    name = name.concat("," + array[i]);
                }
            }

            double price = Double.parseDouble(array[array.length - 1]);

            addFood(restaurantId, category, name, price);
        }
        br.close();
    }

    /*public void saveToRestaurantFile() throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_RESTAURANT_FILE));
        for(int i = 0; i < restaurantList.size(); i++){
            Restaurant r = restaurantList.get(i);
            bw.write(r.id + "," + r.name + "," + r.score + "," + r.price + "," + r.zipcode);
            for(int j = 0; j < r.categories.length; j++){
                bw.write("," + r.categories[j]);
            }
            if(i < restaurantList.size() - 1) {
                bw.write(System.lineSeparator());
            }
        }
        bw.close();
    }

    public  void saveToMenuFile() throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_MENU_FILE));
        for(int i = 0; i < foodList.size(); i++){
            Food f = foodList.get(i);
            bw.write(f.restaurantId + "," + f.category + "," + f.name + "," + f.price);
            if(i < foodList.size() - 1) {
                bw.write(System.lineSeparator());
            }
        }
        bw.close();
    }*/

}
