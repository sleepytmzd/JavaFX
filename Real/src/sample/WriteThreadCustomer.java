package sample;

import util.NetworkUtil;

import java.io.IOException;

public class WriteThreadCustomer implements Runnable{
    private final Thread thr;
    private CustomerMain main;
    public WriteThreadCustomer(CustomerMain main){
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        System.out.println("Entered write");
        while(true){
            //System.out.println(main.ordergot);
            if(main.ordergot){
                try {
                    main.getNetworkUtil().write(main.order);
                } catch (IOException e) {
                    System.out.println("Order jay nai " + e);
                    e.printStackTrace();
                }
                main.ordergot = false;
            }
        }
    }
}
