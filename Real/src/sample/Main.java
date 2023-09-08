package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import server.Restaurant;
import util.NetworkUtil;
import util.Order;

import java.io.IOException;

public class Main extends Application {

    private Restaurant restaurant;
    private Order order;
    private Stage stage;
    private NetworkUtil networkUtil;
    private HomeController shit;

    public Stage getStage() {
        return stage;
    }
    public Restaurant getRestaurant(){ return restaurant; }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }
    public void setOrder(Order order){
        this.order = order;
        System.out.println("Order dise: " + this.order.customerName);
        shit.updateOrder(order);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        connectToServer();
        showLoginPage();
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
        new ReadThread(this);
    }

    public void showLoginPage() throws Exception {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        // Loading the controller
        LoginController controller = loader.getController();
        controller.setMain(this);

        // Set the primary stage
        stage.setTitle("Restaurant cdi");
        stage.setScene(new Scene(root, 400, 250));
        stage.show();
    }

    public void showHomePage(Restaurant restaurant) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Parent root = loader.load();

        // Loading the controller
        HomeController controller = loader.getController();
        controller.setMain(this);
        controller.init(restaurant);
        //controller.updateOrder(order);
        shit = controller;

        this.restaurant = restaurant;
        System.out.println(this.restaurant);

        // Set the primary stage
        stage.setTitle("Tor Restaurant");
        stage.setScene(new Scene(root, 939, 585));
        stage.show();
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // This will launch the JavaFX application
        launch(args);
    }
}
