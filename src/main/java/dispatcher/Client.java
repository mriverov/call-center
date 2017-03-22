package dispatcher;

import java.util.LinkedList;

/**
 * Created by mrivero on 21/3/17.
 */
public class Client extends Thread {
    private Integer id;
    private LinkedList<Integer> calls;

    public Client(Integer id, LinkedList<Integer> calls){
        this.id = id;
        this.calls=calls;
    }

    @Override
    public void run() {
        int sleep = (int) (Math.random() * (15 - 9)) + 9;

        while(true){
            if(!calls.contains(this.id)){
                //make a call
                System.out.println("Client "+ this.id +" making call");
                calls.add(this.id);
                try {
                    sleep(sleep * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
