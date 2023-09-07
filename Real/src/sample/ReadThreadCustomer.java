//package Customer;
package sample;

import javafx.application.Platform;
import server.Restaurant;
import util.LoginDTO;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class ReadThreadCustomer implements Runnable{
    private final Thread thr;
    private final CustomerMain main;

    public ReadThreadCustomer(CustomerMain main){
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run(){
        try{
            while(true) {
                Object o = main.getNetworkUtil().read();
                Object restaurants = main.getNetworkUtil().read();
                if(restaurants != null){
                    System.out.println("Aicheeeeee");
                }
                if(o != null){
                    if(o instanceof String) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                String name = (String) o;
                                List<Restaurant> restaurantList = (List<Restaurant>) restaurants;
                                System.out.println("Customer er naam " + o);
                                try {
                                    main.showHomePage(name, restaurantList);
                                } catch (Exception e) {
                                    System.out.println("Baler customer " + e);
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("customer thread e shomossha " + e);
        }finally{
            try {
                main.getNetworkUtil().closeConnection();
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
}
