//package Customer;
package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class enterController {
    @FXML
    private TextField Name;
    @FXML
    private Button EnterButton;
    public String customerName;
    private CustomerMain main;

    public void EnterAction(ActionEvent actionEvent) {
        customerName = Name.getText();
        try{
            main.getNetworkUtil().write(customerName);
        }catch(Exception e){
            System.out.println("Customer bal " + e);
            e.printStackTrace();
        }
    }

    void setMain(CustomerMain main){this.main = main;}
}
