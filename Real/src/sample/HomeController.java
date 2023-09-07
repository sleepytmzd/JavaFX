package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Order;

public class HomeController {

    @FXML
    private ListView<String> orderList = new ListView<>();
    private Order order;
    private Main main;

    @FXML
    private Label message;

    @FXML
    private ImageView image;

    @FXML
    private Button button;

    public void init(String msg) {
        message.setText(msg);
        if(msg.equals("KFC")){
            Image img = new Image(Main.class.getResourceAsStream("kfc.png"));
            image.setImage(img);
        }
        else if(msg.equals("IHOP")){
            Image img = new Image(Main.class.getResourceAsStream("ihop.png"));
            image.setImage(img);
        }
        else if(msg.equals("Starbucks")){
            Image img = new Image(Main.class.getResourceAsStream("starbucks.png"));
            image.setImage(img);
        }
        else if(msg.equals("McDonalds")){
            Image img = new Image(Main.class.getResourceAsStream("mcdonalds.png"));
            image.setImage(img);
        }
    }

    public void updateOrder(Order order){
        if(this.order == null){
            this.order = order;
        }
        //this.order = order;
        /*for(int i = 0; i < order.foods.size(); i++){
            this.order.foods.add(order.foods.get(i));
        }*/
        else {
            this.order.foods.addAll(order.foods);
        }
        ObservableList<String> foodNames = FXCollections.observableArrayList();
        for(int i = 0; i < this.order.foods.size(); i++){
            foodNames.add(this.order.foods.get(i).name);
        }
        orderList.setItems(foodNames);
        System.out.println("Update korsi to");
    }

    @FXML
    void logoutAction(ActionEvent event) {
        try {
            main.showLoginPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setMain(Main main) {
        this.main = main;
    }

}
