package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.Food;
import server.Restaurant;
import util.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeController {

    public Label Id;
    public Label Name;
    public Label Score;
    public Label Price;
    public Label Zipcode;
    public TableView<Data> menuTable = new TableView<>();
    public Label newOrder;
    @FXML
    private ListView<String> orderList = new ListView<>();
    private List<Order> allOrders = new ArrayList<>();
    private Main main;

    @FXML
    private Label message;

    @FXML
    private ImageView image;

    @FXML
    private Button orderPage;
    private ObservableList<String> orderedfoodNames = FXCollections.observableArrayList();
    private ObservableList<Data> menu = FXCollections.observableArrayList();

    private void initializeColumns(){
        TableColumn<Data, String> foodNameCol = new TableColumn<>("Name");
        foodNameCol.setMinWidth(200);
        foodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Data, String> foodCategoryCol = new TableColumn<>("Category");
        foodCategoryCol.setMinWidth(150);
        foodCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Data, String> foodPriceCol = new TableColumn<>("Price");
        foodPriceCol.setMinWidth(50);
        foodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Data, String> foodOrdersCol = new TableColumn<>("Orders");
        foodOrdersCol.setMinWidth(50);
        foodOrdersCol.setCellValueFactory(new PropertyValueFactory<>("orders"));

        menuTable.getColumns().addAll(foodNameCol, foodCategoryCol, foodPriceCol, foodOrdersCol);
    }

    private void displayOrder() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("displayOrder.fxml"));
        Parent parent = loader.load();

        DisplayOrderController controller = loader.getController();
        controller.init(allOrders);

        Stage displayOrderStage = new Stage();
        Scene displayOrderScene = new Scene(parent, 574, 500);
        displayOrderStage.setTitle("Orders");
        displayOrderStage.setScene(displayOrderScene);
        displayOrderStage.show();
    }

    public void init(Restaurant restaurant) {
        String msg = restaurant.getName();
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
        //ObservableList<String> foodNames = FXCollections.observableArrayList();
        Id.setText(""+ restaurant.getId());
        Name.setText(restaurant.getName());
        Score.setText("" + restaurant.getScore());
        Price.setText(restaurant.getPrice());
        Zipcode.setText(restaurant.getZipcode());

        initializeColumns();

        for(int i = 0; i < restaurant.foods.size(); i++){
            Food f = restaurant.foods.get(i);
            menu.add(new Data(f.name, f.category, f.price));
        }
        menuTable.setItems(menu);

        newOrder.setText("");

        orderPage = new Button("Orders");
        orderPage.setLayoutX(706);
        orderPage.setLayoutY(140);
        orderPage.setOnAction( e_order ->{
            orderPage.setText("Orders");
            newOrder.setText("");
            try {
                displayOrder();
            } catch (Exception e) {
                System.out.println("ahare order page hoilona");
                e.printStackTrace();
            }
        });
        Parent parent = menuTable.getParent();
        AnchorPane root = (AnchorPane) parent;
        root.getChildren().add(orderPage);
    }

    public void updateOrder(Order order){
        /*if(this.order == null){
            this.order = order;
        }
        //this.order = order;
        /*for(int i = 0; i < order.foods.size(); i++){
            this.order.foods.add(order.foods.get(i));
        }*/
        /*else {
            this.order.foods.addAll(order.foods);
            this.order.restaurantId = order.restaurantId;
            this.order.customerName = order.customerName;
        }
        ObservableList<String> foodNames = FXCollections.observableArrayList();*/
        allOrders.add(order);
        orderedfoodNames.clear();
        for(int i = 0; i < order.foods.size(); i++){
            Food f = order.foods.get(i);
            orderedfoodNames.add(f.name);
            //orderedfoodNames.add(f.name + " x" + order.count.get(i) + " ,served to: " + order.customerName);

            for(int j = 0; j < menu.size(); j++){
                if(f.name.equals(menu.get(j).getName())){
                    //menu.get(j).incrementOrders();
                    menu.get(j).incrementOrders(order.count.get(i));
                }
            }
        }
        orderList.setItems(orderedfoodNames);
        menuTable.setItems(menu);
        System.out.println("Update korsi to");

        //Date date = new Date();
        //Button orderPage = new Button("New Order recieved!");

        orderPage.setText("Check Orders");
        orderPage.setLayoutX(706);
        newOrder.setText("New Order Received!");
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
