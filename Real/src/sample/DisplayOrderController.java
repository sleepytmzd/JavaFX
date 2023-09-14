package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import server.Food;
import util.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayOrderController {

    @FXML
    private Label pendingOrdersCount;
    @FXML
    private Label confirmedOrdersCount;
    @FXML
    private ScrollPane confirmedOrderScroll;
    @FXML
    private Label orderCount;
    @FXML
    private ScrollPane pendingOrderScroll;

    private Main main;
    private List<Order> allOrders;
    private List<Pane> confirmedOrderPanes;
    private List<Pane> pendingOrderPanes;
    private VBox pendingOrderBox = new VBox();
    private VBox confirmedOrderBox = new VBox();


    private Pane setHeader(Order order){
        Pane header = new Pane();

        Label nameLabel = new Label();
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 19));
        nameLabel.setPrefSize(400, 40);
        nameLabel.setText("Customer: " + order.customerName);

        Label dateLabel = new Label();
        dateLabel.setFont(new Font(12));
        dateLabel.setPrefSize(400,20);
        dateLabel.setLayoutY(40);
        dateLabel.setText("Timestamp: " + order.date.toString());

        header.setPrefSize(400, 60);
        header.getChildren().add(nameLabel);
        header.getChildren().add(dateLabel);

        return header;
    }
    private Pane setItem(Food food, int amount){
        Pane foodPane = new Pane();

        Label foodName = new Label();
        foodName.setFont(new Font(17));
        foodName.setPrefSize(400, 30);
        foodName.setText(food.getName());

        Label others = new Label();
        others.setFont(new Font(12));
        others.setPrefSize(400, 20);
        others.setLayoutY(30);
        others.setText("Amount: " + amount + " ,Price: " + food.getPrice());

        Label category = new Label();
        category.setFont(new Font(12));
        category.setPrefSize(400, 20);
        category.setLayoutY(50);
        category.setText("Category: " + food.getCategory());

        foodPane.setPrefSize(400, 85);
        foodPane.getChildren().addAll(foodName, others, category);
        foodPane.setLayoutX(10);

        return foodPane;
    }
    private Pane addOrder(Order order){
        Pane orderPane = new Pane();

        Pane header = setHeader(order);
        Pane[] items = new Pane[order.foods.size()];
        for(int i = 0; i < order.foods.size(); i++){
            items[i] = setItem(order.foods.get(i), order.count.get(i));
            items[i].setLayoutY(60 + i * 85);
        }

        orderPane.getChildren().add(header);
        orderPane.getChildren().addAll(items);

        return orderPane;
    }

    public void init(List<Order> allOrders, List<Pane> pendingOrderPanes, List<Pane> confirmedOrderPanes){
        this.allOrders = allOrders;
        this.pendingOrderPanes = pendingOrderPanes;
        this.confirmedOrderPanes = confirmedOrderPanes;

        pendingOrderScroll.setContent(pendingOrderBox);
        pendingOrderScroll.setFitToWidth(true);

        confirmedOrderScroll.setContent(confirmedOrderBox);
        confirmedOrderScroll.setFitToWidth(true);

        //Pane[] orderPanes = new Pane[allOrders.size()];

        if(pendingOrderPanes.size() + confirmedOrderPanes.size() != allOrders.size()) {
            //List<Pane> orderPanes = new ArrayList<>();
            for (int i = pendingOrderPanes.size() + confirmedOrderPanes.size(); i < allOrders.size(); i++) {

                Pane tempOrderPane = addOrder(allOrders.get(i));
                pendingOrderPanes.add(tempOrderPane);
                tempOrderPane.setOnMouseClicked(e -> {
                    //confirmedOrders.add(allOrders.get(i));
                    //orderConfirmation(tempOrderPane, confirmedOrderBox);
                    pendingOrderPanes.remove(tempOrderPane);
                    pendingOrderBox.getChildren().remove(tempOrderPane);
                    confirmedOrderPanes.add(tempOrderPane);
                    confirmedOrderBox.getChildren().add(tempOrderPane);
                    confirmedOrderScroll.setVvalue(1.0);

                    pendingOrdersCount.setText("Pending orders: (" + pendingOrderPanes.size() + ")");
                    confirmedOrdersCount.setText("Confirmed orders : (" + confirmedOrderPanes.size() + ")");
                });
            }
        }
        //orderBox.getChildren().addAll(orderPanes);

        for (int i = 0; i < pendingOrderPanes.size(); i++) {
            pendingOrderBox.getChildren().add(0, pendingOrderPanes.get(i));
        }

        for(int i = 0; i < confirmedOrderPanes.size(); i++){
            confirmedOrderBox.getChildren().add(confirmedOrderPanes.get(i));
        }

        orderCount.setText("Total order count: " + allOrders.size());
        pendingOrdersCount.setText("Pending orders: (" + pendingOrderPanes.size() + ")");
        confirmedOrdersCount.setText("Confirmed orders : (" + confirmedOrderPanes.size() + ")");
    }
}
