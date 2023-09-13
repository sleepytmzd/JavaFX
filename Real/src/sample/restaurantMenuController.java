package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server.Food;

import java.util.List;
import java.util.Objects;

class listItem extends Node {
    public Pane pane;
    public Label foodName;
    public Label amount;
    public listItem(){
        pane = new Pane();
        foodName = new Label();
        amount = new Label();

        pane.getChildren().add(foodName);
        pane.getChildren().add(amount);

        pane.setPrefSize(250,50);
        foodName.setFont(new Font(17));
        foodName.setPrefSize(150, 50);
        amount.setFont(new Font(12));
        amount.setPrefSize(75, 50);
    }

    public void setFoodName(String name){
        foodName.setText(name);
    }
    public void setAmount(int amount) {
        this.amount.setText(String.valueOf(amount));
    }
}

public class restaurantMenuController {
    public TableView<Food> menuTable = new TableView<>();
    public TextField enterName;
    public TextField categoryFilter;
    public TextField priceFilterLowerBound;
    public TextField priceFilterUpperBound;
    public Label filteredFoodsCount;
    private VBox cartbox = new VBox();
    public ScrollPane cartscroll = new ScrollPane();
    //@FXML
    // private ListView<listItem> cart = new ListView<>();
    @FXML
    private Label name;
    //@FXML
    //private ListView<String> foodMenu = new ListView<>();

    private CustomerMain main;
    private String restaurantName;
    private List<Food> foodMenu;
    private ObservableList<Food> foods = FXCollections.observableArrayList();
    //private ObservableList<listItem> cartItems = FXCollections.observableArrayList();
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

    private void removeFromCart(Food foodToRemove, int amountToRemoveIdx){
        main.order.foods.remove(foodToRemove);
        main.order.count.remove(amountToRemoveIdx);
        cartbox.getChildren().remove(amountToRemoveIdx);

        System.out.println("In removeFromCart, " + main.order.foods.size() + " " + main.order.count.size());
        for(int i = 0; i < main.order.foods.size(); i++){
            System.out.println(main.order.foods.get(i));
        }
    }
    private void addInCart(Food food, int cnt){
        Pane pane = new Pane();
        Label foodName = new Label();
        Label amount = new Label();

        pane.getChildren().add(foodName);
        pane.getChildren().add(amount);

        pane.setPrefSize(250,50);
        foodName.setFont(new Font(17));
        foodName.setPrefSize(250, 30);
        foodName.setLayoutY(0);
        foodName.setText(food.name);
        amount.setFont(new Font(12));
        amount.setPrefSize(250, 20);
        amount.setLayoutY(30);
        amount.setText("Amount: " + cnt);

        cartbox.getChildren().add(pane);
        //cartItems.add(ls);

        pane.setOnMouseClicked( epane ->{
            Label lab = (Label) pane.getChildren().get(0);
            String foodNameInCart = lab.getText();

            Food found = main.order.foods.get(0);
            int idx = 0;
            for(int i = 1; i < main.order.foods.size(); i++){
                Food f = main.order.foods.get(i);
                if(f.name.equals(foodNameInCart)){
                    found = f;
                    idx = i;
                }
            }
            final Food removeFood = found;
            final int removeAmountIdx = idx;

            Pane removePane = new Pane();

            Label name = new Label();
            name.setFont(new Font(19));
            name.setText(found.name);
            name.setPrefSize(300, 50);
            name.setLayoutX(5);

            Label others = new Label();
            others.setFont(new Font(15));
            others.setText("Amount: " + main.order.count.get(idx) + " ,Price: " + found.price);
            others.setPrefSize(300, 50);
            others.setLayoutY(50);
            others.setLayoutX(5);

            Label category = new Label();
            category.setFont(new Font(15));
            category.setText("Category: " + found.category);
            category.setPrefSize(300, 50);
            category.setLayoutY(100);
            category.setLayoutX(5);

            Label ask = new Label();
            ask.setText("Remove this item from cart:");
            ask.setPrefSize(150, 30);
            ask.setLayoutY(150);
            ask.setLayoutX(5);

            Button confirmation = new Button("Yes");
            confirmation.setLayoutX(160);
            confirmation.setLayoutY(150);

            removePane.getChildren().addAll(name, others, category, ask, confirmation);
            Scene removeScene = new Scene(removePane, 300, 200);
            Stage removeStage = new Stage();

            confirmation.setOnAction( ce -> {
                removeFromCart(removeFood, removeAmountIdx);
                removeStage.close();
            });


            removeStage.setScene(removeScene);
            removeStage.setTitle("Remove");
            removeStage.show();
        });

        System.out.println("In addInCart, " + main.order.foods.size() + " " + main.order.count.size());
        for(int i = 0; i < main.order.foods.size(); i++){
            System.out.println(main.order.foods.get(i));
        }
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

        //cart.setItems(cartItems);
        //box.setPrefSize(250,200);
        //box.setMaxHeight(200);

        cartscroll.setContent(cartbox);
        cartscroll.setFitToWidth(true);
        if(!main.order.foods.isEmpty()) {
            for(int i = 0; i < main.order.foods.size(); i++){
                addInCart(main.order.foods.get(i), main.order.count.get(i));
            }
        }

        //cartscroll.setPrefViewportHeight(200);

        initializeColumns();

        foods.addAll(foodmenu);
        menuTable.setItems(foods);
        filteredFoodsCount.setText("Available Foods: " + foods.size());
        menuTable.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue == null){
                        //newValue = oldValue;
                        return;
                    }
                    System.out.println(newValue);

                    Pane getAmount = new Pane();
                    TextField tf = new TextField();
                    tf.setLayoutX(85);
                    tf.setLayoutY(0);
                    tf.setPrefSize(100, 30);
                    Label label = new Label("Enter amount:");
                    label.setLayoutX(3);
                    label.setLayoutY(7);
                    getAmount.getChildren().add(label);
                    getAmount.getChildren().add(tf);
                    Scene amountScene = new Scene(getAmount, 190, 45);
                    Stage amountStage = new Stage();
                    tf.setOnKeyTyped(e -> {
                        if(e.getCharacter().equals("\r")){
                            System.out.println("Kaj hoise");
                            final String bal = tf.getText();
                            final int cnt = Integer.parseInt(bal);
                            main.order.count.add(cnt);

                            addInCart(newValue, cnt);
                            //cartItems.add(newValue.name + " ,x" + cnt);

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
        /*foods.clear();
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

        menuTable.setItems(foods);*/

        ObservableList<Food> temp = FXCollections.observableArrayList();
        temp.addAll(foods);
        foods.clear();

        for(int i = 0; i < foodMenu.size(); i++){
            Food f = foodMenu.get(i);
            if(f.name.toLowerCase().contains(filterName.toLowerCase()) && f.category.toLowerCase().contains(filterCategory.toLowerCase()) && f.price >= filterPriceLower && f.price <= filterPriceUpper){
                foods.add(f);
            }
        }
        temp.clear();
        filteredFoodsCount.setText("Available Foods: " + foods.size());
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
