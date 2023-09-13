//package Customer;
package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import server.Food;
import server.Restaurant;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CusthomeController {
    //public ListView<String> orderedFoodsList = new ListView<>();
    public Label orderConfirmation;
    public Label totalPrice;
    public TextField nameFilter;
    public TextField scoreFilterLowerBound;
    public TextField scoreFilterUpperBound;
    public TextField priceFilter;
    public ScrollPane ordersScroll = new ScrollPane();
    public Label filteredRestaurantCount;
    private VBox orderBox = new VBox();
    @FXML
    private TableView<Restaurant> restaurantListView = new TableView<>();
    @FXML
    private Button placeOrder;
    @FXML
    private Button Logout;
    private CustomerMain main;
    @FXML
    private Label customerName;

    private String filterName;
    private String filterPrice;
    private Double filterScoreLower;
    private Double filterScoreUpper;
    private ObservableList<Restaurant> restaurantNames = FXCollections.observableArrayList();
    private List<Restaurant> restaurantList;


    public void setMain(CustomerMain main){ this.main = main; }
    private void initialize_columns(){
        TableColumn<Restaurant, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(150);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Restaurant, Double> scoreCol = new TableColumn<>("Score");
        scoreCol.setMinWidth(100);
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<Restaurant, String> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(50);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Restaurant, String> zipcodeCol = new TableColumn<>("Zipcode");
        zipcodeCol.setMinWidth(100);
        zipcodeCol.setCellValueFactory(new PropertyValueFactory<>("zipcode"));

        restaurantListView.getColumns().addAll(nameCol, scoreCol, priceCol, zipcodeCol);
    }

    private void addInOrder(Food foodInCart, int foodAmount){
        Pane pane = new Pane();

        Label foodName = new Label();
        foodName.setFont(new Font(17));
        foodName.setPrefSize(222, 30);
        foodName.setText(foodInCart.name);

        Label others = new Label();
        others.setFont(new Font(12));
        others.setPrefSize(222,20);
        others.setText("Amount: " + foodAmount + "  ,Price: " + foodInCart.price);
        others.setLayoutY(30);

        Label category = new Label();
        category.setPrefSize(222, 30);
        category.setText("Category: " + foodInCart.category);
        category.setLayoutY(50);

        pane.getChildren().addAll(foodName, others, category);
        orderBox.getChildren().add(pane);
        System.out.println("order added: " + foodInCart);
    }

    public void init(String customerName, List<Restaurant> restaurantList) {
        this.customerName.setText("Welcome, " + customerName);
        this.restaurantList = restaurantList;

        /*for(int i = 0; i < restaurantList.size(); i++){
            restaurantNames.add(restaurantList.get(i));
        }*/
        restaurantNames.addAll(restaurantList);
        initialize_columns();
        restaurantListView.setItems(restaurantNames);
        filteredRestaurantCount.setText("Total Restaurants: " + restaurantNames.size());

        restaurantListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue == null){
                        return;
                    }
                    int id = 0;
                    for(int i = 0; i < restaurantList.size(); i++){
                        if(newValue.getName().equals(restaurantList.get(i).name)){
                            id = i + 1;
                            break;
                        }
                    }
                    //main.order.restaurantId = id;

                    try {
                        main.showRestaurantMenuPage(newValue.getName());
                    } catch (Exception e) {
                        System.out.println("Restaurant menu page dekhay na " + e);
                        e.printStackTrace();
                    }
                }
        );

        /*ObservableList<String> orderedFoodNames = FXCollections.observableArrayList();
        for(int i = 0; i < main.order.foods.size(); i++){
            orderedFoodNames.add(main.order.foods.get(i).name + " ,x" + main.order.count.get(i));
        }
        orderedFoodsList.setItems(orderedFoodNames);*/

        ordersScroll.setContent(orderBox);
        ordersScroll.setFitToWidth(true);
        for(int i = 0; i < main.order.foods.size(); i++){
            addInOrder(main.order.foods.get(i), main.order.count.get(i));
        }

        if(main.order.foods.isEmpty()){
            totalPrice.setText("");
        }
        else{
            double total = 0.0;
            for(int i = 0; i < main.order.foods.size(); i++){
                total += main.order.foods.get(i).price * main.order.count.get(i);
            }
            totalPrice.setText("Total price: " + String.valueOf(total));
        }

        filterName = "";
        filterPrice = "";
        filterScoreLower = 0.0;
        filterScoreUpper = Double.MAX_VALUE;

    }

    public void LogoutAction(ActionEvent actionEvent){
        try {
            main.showLoginPage();
        } catch (Exception e) {
            System.out.println("Bair hoy na " + e);
            e.printStackTrace();
        }
    }

    public void placeOrderAction(ActionEvent actionEvent) {
        main.ordergot = true;
        main.order.date = new Date();
        //placeOrder.setText("Order Placed!");
        AnchorPane parent = (AnchorPane) placeOrder.getParent();
        parent.getChildren().remove(placeOrder);
        orderConfirmation.setText("Order placed successfully!");
        try {
            System.out.println("Custhome theke order pathacchi ");
            for(int i = 0; i < main.order.foods.size(); i++){
                System.out.println(main.order.foods.get(i));
            }
            main.getNetworkUtil().write(main.order);
            //main.order.clear();
        } catch (IOException e) {
            System.out.println("Order jay nai ken " + e);
            e.printStackTrace();
        }
    }

    public void menuDoneAction(ActionEvent actionEvent) {
    }

    private void filter(){
        ObservableList<Restaurant> temp = FXCollections.observableArrayList();
        temp.addAll(restaurantList);
        restaurantNames.clear();

        for(int i = 0; i < temp.size(); i++){
            Restaurant r = temp.get(i);
            if(r.getName().toLowerCase().contains(filterName.toLowerCase()) && r.getScore() >= filterScoreLower && r.getScore() <= filterScoreUpper){
                restaurantNames.add(r);
            }
        }
        temp.clear();

        if(!(Objects.equals(filterPrice, ""))){
            temp.addAll(restaurantNames);
            restaurantNames.clear();

            for(int i = 0; i < temp.size(); i++){
                if(temp.get(i).getPrice().equals(filterPrice)){
                    restaurantNames.add(temp.get(i));
                }
            }
            temp.clear();
        }
        filteredRestaurantCount.setText("Total Restaurants: " + restaurantNames.size());
    }
    public void nameFilterAction(KeyEvent keyEvent) {
        String restaurantName = nameFilter.getText();
        System.out.println(restaurantName);
        filterName = restaurantName;
        filter();
    }

    public void scoreLowerBoundAction(KeyEvent keyEvent) {
        String lower = scoreFilterLowerBound.getText();
        if(Objects.equals(lower, "")){
            lower = "0.0";
        }
        Double lowerScore = Double.parseDouble(lower);
        System.out.println(lowerScore);
        filterScoreLower = lowerScore;
        filter();
    }

    public void scoreUpperBoundAction(KeyEvent keyEvent) {
        String upper = scoreFilterUpperBound.getText();
        if(Objects.equals(upper, "")){
            upper = String.valueOf(Double.MAX_VALUE);
        }
        Double upperScore = Double.parseDouble(upper);
        System.out.println(upperScore);
        filterScoreUpper = upperScore;
        filter();
    }

    public void priceFilterAction(KeyEvent keyEvent) {
        String price = priceFilter.getText();
        System.out.println(price);
        filterPrice = price;
        filter();
    }
}
