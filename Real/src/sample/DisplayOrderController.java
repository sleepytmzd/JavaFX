package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import server.Food;
import util.Order;

import java.util.Date;
import java.util.List;

public class DisplayOrderController {

    @FXML
    private Label orderCount;
    @FXML
    private ScrollPane orderScroll;

    private Main main;
    private List<Order> allOrders;

    private Pane setHeader(Order order){
        Pane header = new Pane();

        Label nameLabel = new Label();
        nameLabel.setFont(new Font(19));
        nameLabel.setPrefSize(400, 40);
        nameLabel.setText("Customer: " + order.customerName);

        Label dateLabel = new Label();
        dateLabel.setFont(new Font(12));
        dateLabel.setPrefSize(400,20);
        dateLabel.setLayoutY(40);
        dateLabel.setText("Timestamp: " + order.date.toString());

        header.setPrefSize(400, 60);
        header.getChildren().addAll(nameLabel, dateLabel);

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

    public void init(List<Order> allOrders){
        this.allOrders = allOrders;

        VBox orderBox = new VBox();
        orderScroll.setContent(orderBox);
        orderScroll.setFitToWidth(true);
        Pane[] orderPanes = new Pane[allOrders.size()];
        for(int i = 0; i < allOrders.size(); i++){
            orderPanes[i] = addOrder(allOrders.get(i));

        }
        orderBox.getChildren().addAll(orderPanes);
        orderCount.setText("Total orders: " + allOrders.size());
    }
}
