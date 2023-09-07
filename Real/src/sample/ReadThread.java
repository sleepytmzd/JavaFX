package sample;

import javafx.application.Platform;
import server.Restaurant;
import util.LoginDTO;
import util.Order;

import java.io.IOException;

public class ReadThread implements Runnable {
    private final Thread thr;
    private final Main main;

    public ReadThread(Main main) {
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = main.getNetworkUtil().read();
                if (o != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(o instanceof Restaurant){
                                Restaurant restaurant = (Restaurant) o;
                                System.out.println(restaurant.id + ", " + restaurant.name);
                                try{
                                    main.showHomePage(restaurant);
                                }catch(Exception e){
                                    System.out.println("Bal, " + e);
                                    e.printStackTrace();
                                }
                            }
                            if(o instanceof LoginDTO){
                                main.showAlert();
                            }
                            if(o instanceof Order){
                                System.out.println("restaurant e order ashchee");
                                main.setOrder((Order)o);
                            }
                        }
                    });
                    /*if (o instanceof Restaurant) {
                        //LoginDTO loginDTO = (LoginDTO) o;
                        Restaurant restaurant = (Restaurant) o;
                        //System.out.println(loginDTO.getUserName());
                        System.out.println(restaurant.id + ", " + restaurant.name);
                        //System.out.println(loginDTO.isStatus());
                        // the following is necessary to update JavaFX UI components from user created threads
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                /*if (loginDTO.isStatus()) {
                                    try {
                                        Object o = main.getNetworkUtil().read();
                                        if (o instanceof Restaurant) {
                                            System.out.println("yay");
                                        }
                                    }catch(Exception e){
                                        System.out.println("bal, " + e);
                                    }
                                    try {
                                        main.showHomePage(loginDTO.getUserName());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    main.showAlert();
                                }
                                try{
                                    main.showHomePage(restaurant);
                                }catch(Exception e){
                                    System.out.println("bal" + e);
                                    e.printStackTrace();
                                }

                            }
                        });*/
                    /*}
                    else if(o instanceof LoginDTO){
                        main.showAlert();
                    }*/
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                main.getNetworkUtil().closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



