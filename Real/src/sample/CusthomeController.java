//package Customer;
package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import server.Restaurant;

import java.io.IOException;
import java.util.List;

public class CusthomeController {
    public ListView<String> orderedFoodsList = new ListView<>();
    @FXML
    private ListView<String> restaurantListView = new ListView<>();
    @FXML
    private Button placeOrder;
    @FXML
    private Button Logout;
    private CustomerMain main;
    @FXML
    private Label customerName;


    public void setMain(CustomerMain main){ this.main = main; }
    public void init(String customerName, List<Restaurant> restaurantList) {
        this.customerName.setText(customerName);
        ObservableList<String> restaurantNames = FXCollections.observableArrayList();
        for(int i = 0; i < restaurantList.size(); i++){
            restaurantNames.add(restaurantList.get(i).name);
        }
        restaurantListView.setItems(restaurantNames);

        restaurantListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    int id = 0;
                    for(int i = 0; i < restaurantList.size(); i++){
                        if(newValue.equals(restaurantList.get(i).name)){
                            id = i + 1;
                            break;
                        }
                    }
                    main.order.restaurantId = id;

                    try {
                        main.showRestaurantMenuPage(newValue);
                    } catch (Exception e) {
                        System.out.println("Restaurant menu page dekhay na " + e);
                        e.printStackTrace();
                    }
                }
        );

        ObservableList<String> orderedFoodNames = FXCollections.observableArrayList();
        for(int i = 0; i < main.order.foods.size(); i++){
            orderedFoodNames.add(main.order.foods.get(i).name);
        }
        orderedFoodsList.setItems(orderedFoodNames);
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
        placeOrder.setText("Order Placed!");
        try {
            main.getNetworkUtil().write(main.order);
        } catch (IOException e) {
            System.out.println("Order jay nai ken " + e);
            e.printStackTrace();
        }
    }

    public void menuDoneAction(ActionEvent actionEvent) {
    }
}
