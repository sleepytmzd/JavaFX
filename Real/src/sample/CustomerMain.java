//package Customer;
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import server.Restaurant;
import util.NetworkUtil;
import java.util.ArrayList;
import java.util.List;
import util.Order;

import java.io.IOException;

public class CustomerMain extends Application {

    public List<Restaurant> restaurantList;
    public String customerName;
    public Order order;
    public boolean ordergot = false;
    private Stage stage;
    private NetworkUtil networkUtil;

    public Stage getStage() {
        return stage;
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        try {
            connectToServer();
        }catch(Exception e){
            System.out.println("server e connect hoyna " + e);
            e.printStackTrace();
        }
        try {
            showLoginPage();
        }catch(Exception e){
            System.out.println("login page dekhay na " + e);
            e.printStackTrace();
        }
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
        new ReadThreadCustomer(this);
        //new WriteThreadCustomer(this);
    }

    public void showLoginPage() throws Exception {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("enter.fxml"));
        Parent root = loader.load();

        // Loading the controller
        enterController controller = loader.getController();
        controller.setMain(this);
        controller.init();

        order = new Order();

        // Set the primary stage
        stage.setTitle("Customer Entry");
        stage.setScene(new Scene(root, 719, 343));
        stage.show();
    }

    public void showHomePage(String customerName, List<Restaurant> restaurantList) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("custhome.fxml"));
        Parent root = loader.load();

        // Loading the controller
        CusthomeController controller = loader.getController();
        controller.setMain(this);
        controller.init(customerName, restaurantList);

        //order = new Order(customerName);
        //order.restaurantId = 1;
        order.customerName = customerName;
        this.restaurantList = restaurantList;
        this.customerName = customerName;
        for(int i = 0; i < restaurantList.size(); i++){
            System.out.println(restaurantList.get(i));
        }
        //order.addFood(restaurantList.get(0).getFoods().get(0));

        // Set the primary stage
        stage.setTitle("Home");
        stage.setScene(new Scene(root, 940, 525));
        stage.show();
    }

    public void showRestaurantMenuPage(String restaurantName) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("restaurantMenu.fxml"));
        Parent root = loader.load();

        int r = 0;
        for(int i = 0; i < restaurantList.size(); i++){
            if(restaurantName.equals(restaurantList.get(i).name)){
                r = i;
                break;
            }
        }

        restaurantMenuController controller = loader.getController();
        controller.setMain(this);
        controller.init(restaurantList.get(r).name, restaurantList.get(r).getFoods());

        stage.setTitle("Restaurant Menu");
        stage.setScene(new Scene(root, 863, 657));
        stage.show();
    }

    public static void main(String[] args) {
        // This will launch the JavaFX application
        launch(args);
    }
}
