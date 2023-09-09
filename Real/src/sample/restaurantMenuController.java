package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import server.Food;

import java.util.List;
import java.util.Objects;

public class restaurantMenuController {
    public TableView<Food> menuTable = new TableView<>();
    public TextField enterName;
    public TextField categoryFilter;
    public TextField priceFilterLowerBound;
    public TextField priceFilterUpperBound;
    @FXML
    private Label name;
    //@FXML
    //private ListView<String> foodMenu = new ListView<>();
    private CustomerMain main;
    private String restaurantName;
    private List<Food> foodMenu;
    private ObservableList<Food> foods = FXCollections.observableArrayList();
    private String filterName;
    private String filterCategory;
    private Double filterPriceLower;
    private Double filterPriceUpper;

    private void initializeColumns(){
        TableColumn<Food, String> foodNameCol = new TableColumn<>("Name");
        foodNameCol.setMinWidth(250);
        foodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Food, String> foodCategoryCol = new TableColumn<>("Category");
        foodCategoryCol.setMinWidth(150);
        foodCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Food, Double> foodPriceCol = new TableColumn<>("Price");
        foodPriceCol.setMinWidth(100);
        foodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        menuTable.getColumns().addAll(foodNameCol, foodCategoryCol, foodPriceCol);
    }

    public void init(String restaurantName, List<Food> foodmenu){
        this.restaurantName = restaurantName;
        name.setText(this.restaurantName);
        this.foodMenu = foodmenu;

        /*ObservableList<String> foodNames = FXCollections.observableArrayList();
        for(int i = 0; i < foodmenu.size(); i++){
            foodNames.add(foodmenu.get(i).name);
        }
        foodMenu.setItems(foodNames);
        foodMenu.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    int f = 0;
                    for(int i = 0; i < foodmenu.size(); i++){
                        if(newValue.equals(foodmenu.get(i).name)){
                            f = i;
                            break;
                        }
                    }
                    main.order.addFood(foodmenu.get(f));
                }
        );*/

        initializeColumns();

        foods.addAll(foodmenu);
        menuTable.setItems(foods);
        menuTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue == null){
                        //newValue = oldValue;
                        return;
                    }
                    System.out.println(newValue);

                    String amount;
                    VBox getAmount = new VBox();
                    TextField tf = new TextField();
                    getAmount.getChildren().add(tf);
                    Scene amountScene = new Scene(getAmount, 300, 200);
                    Stage amountStage = new Stage();
                    tf.setOnKeyTyped(e -> {
                        if(e.getCharacter().equals("\r")){
                            System.out.println("Kaj hoise");
                            final String bal = tf.getText();
                            final int cnt = Integer.parseInt(bal);
                            main.order.count.add(cnt);
                            amountStage.close();
                        }
                    });
                    amountStage.setScene(amountScene);
                    amountStage.setTitle("Amount");
                    amountStage.show();

                    main.order.restaurantId = newValue.restaurantId;
                    main.order.addFood(newValue);
                }
        );

        filterName = "";
        filterCategory = "";
        filterPriceLower = 0.0;
        filterPriceUpper = 10000.0;
    }

    public void setMain(CustomerMain main){this.main = main;}

    public void foodSelectionDoneAction(ActionEvent actionEvent) {
        try {
            //main.showRestaurantMenuPage(restaurantName);
            main.showHomePage(main.customerName, main.restaurantList);
        } catch (Exception e) {
            System.out.println("Back jaitesena kn " + e);
            e.printStackTrace();
        }
    }

    private void filter(){
        foods.clear();
        for(int i = 0; i < foodMenu.size(); i++){
            if(foodMenu.get(i).name.toLowerCase().contains(filterName.toLowerCase())){
                foods.add(foodMenu.get(i));
            }
        }

        ObservableList<Food> temp = FXCollections.observableArrayList();
        temp.addAll(foods);
        foods.clear();

        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).category.toLowerCase().contains(filterCategory.toLowerCase())){
                foods.add(temp.get(i));
            }
        }
        temp.clear();

        temp.addAll(foods);
        foods.clear();

        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).price >= filterPriceLower){
                foods.add(temp.get(i));
            }
        }
        temp.clear();

        temp.addAll(foods);
        foods.clear();

        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).price <= filterPriceUpper){
                foods.add(temp.get(i));
            }
        }

        menuTable.setItems(foods);
    }

    public void nameEntered(KeyEvent keyEvent) {
        if(keyEvent.getCharacter().equals("\r")){
            System.out.println("Enter chap aicheee");
        }
        String foodName = enterName.getText();
        if(foodName == null){
            System.out.println("null name paisi");
            foodName = "";
        }
        System.out.println(foodName);

        /*ObservableList<Food> temp = FXCollections.observableArrayList();
        temp.addAll(foods);

        foods.clear();
        for(int i = 0; i < temp.size(); i++){
            if(temp.get(i).name.toLowerCase().contains(foodName.toLowerCase())){
                foods.add(temp.get(i));
            }
        }
        menuTable.setItems(foods);*/

        filterName = foodName;
        filter();
    }

    public void categoryFilterAction(KeyEvent keyEvent) {
        String categoryName = categoryFilter.getText();
        if(categoryName == null){
            System.out.println("Null category paisi");
            categoryName = "";
        }
        System.out.println(categoryName);

        filterCategory = categoryName;
        filter();
    }

    public void priceLowerBoundAction(KeyEvent keyEvent) {
        String lower = priceFilterLowerBound.getText();
        if(Objects.equals(lower, "")){
            System.out.println("Null lower paisi");
            lower = "0.0";
        }
        Double lowerBound = Double.parseDouble(lower);
        System.out.println(lowerBound);

        filterPriceLower = lowerBound;
        filter();
    }

    public void priceUpperBoundAction(KeyEvent keyEvent) {
        String upper = priceFilterUpperBound.getText();
        if(Objects.equals(upper, "")){
            System.out.println("Null upper paisi");
            upper = "10000.0";
        }
        Double upperBound = Double.parseDouble(upper);
        System.out.println(upperBound);

        filterPriceUpper = upperBound;
        filter();
    }

    public void goBack(ActionEvent actionEvent) {
        try{
            main.showHomePage(main.customerName, main.restaurantList);
        }catch(Exception e){
            System.out.println("Back jayna " + e);
            e.printStackTrace();
        }
    }
}
