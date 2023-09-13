package server;

import util.LoginDTO;
import util.NetworkUtil;
import util.Order;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ReadThreadServer implements Runnable {
    private final Thread thr;
    private final NetworkUtil networkUtil;
    public HashMap<String, String> userMap;
    private DatabaseManager dbms;
    private Server server;


    public ReadThreadServer(Server s, HashMap<String, String> map, NetworkUtil networkUtil, DatabaseManager dbms) {
        server = s;
        this.userMap = map;
        this.dbms = dbms;
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = networkUtil.read();
                System.out.println("New object paisi in server thread");
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        LoginDTO loginDTO = (LoginDTO) o;
                        String password = userMap.get(loginDTO.getUserName());
                        loginDTO.setStatus(loginDTO.getPassword().equals(password));
                        //networkUtil.write(loginDTO);

                        if(loginDTO.isStatus()){
                            int id = Integer.parseInt(loginDTO.getUserName());
                            Restaurant r = dbms.getAllRestaurants().get(id-1);
                            networkUtil.write(r);
                            server.addRestaurantMap(r.id, networkUtil);
                        }

                        else{
                            networkUtil.write(loginDTO);
                        }
                    }
                    if(o instanceof String){
                        System.out.println("Customer aiche");
                        networkUtil.write(o);
                        networkUtil.write(dbms.getAllRestaurants());
                    }
                    if(o instanceof Order){
                        System.out.println("Order aicheee");
                        Order order = (Order) o;
                        System.out.println(order.customerName + " cdrvy order korse");
                        for(int i = 0; i < order.foods.size(); i++){
                            System.out.println(order.foods.get(i));
                        }
                        NetworkUtil nu = server.getRestaurantNet(order.restaurantId);
                        nu.write(order);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



