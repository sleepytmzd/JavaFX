package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import server.Food;

import java.util.List;

public class restaurantMenuController {
    @FXML
    private Label name;
    @FXML
    private ListView<String> foodMenu = new ListView<>();
    private CustomerMain main;
    private String restaurantName;

    public void init(String restaurantName, List<Food> foodmenu){
        this.restaurantName = restaurantName;
        name.setText(this.restaurantName);
        ObservableList<String> foodNames = FXCollections.observableArrayList();
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
        );
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
}
