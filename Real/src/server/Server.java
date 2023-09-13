package server;

import util.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private ServerSocket serverSocket;
    public HashMap<String, String> userMap;
    public HashMap<Integer, NetworkUtil> restaurantMap;
    public DatabaseManager dbms;

    Server() {
        userMap = new HashMap<>();
        userMap.put("1", "a");
        userMap.put("2", "b");
        userMap.put("3", "c");
        userMap.put("4", "d");
        restaurantMap = new HashMap<>();

        dbms = new DatabaseManager();
        dbms.showRestaurantFoods();

        try {
            serverSocket = new ServerSocket(33333);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client peyechi");
                serve(clientSocket);
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public void serve(Socket clientSocket) throws IOException {
        NetworkUtil networkUtil = new NetworkUtil(clientSocket);
        new ReadThreadServer(this, userMap, networkUtil, dbms);
    }

    public static void main(String[] args) {
        new Server();
    }

    public void addRestaurantMap(Integer id, NetworkUtil networkUtil){
        restaurantMap.put(id, networkUtil);
        System.out.println("Map e dhukseeee " + id);
    }
    public NetworkUtil getRestaurantNet(Integer id){
        return restaurantMap.get(id);
    }
}
