package dispacher;

import model.impl.Director;
import model.impl.Operator;
import model.impl.Supervisor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by mrivero on 21/3/17.
 */
public class Dispatcher extends Thread {

    private static Semaphore mutex = new Semaphore(1);


    private List<Director> directors = new ArrayList<Director>();
    private List<Supervisor> supervisors = new ArrayList<Supervisor>();
    private List<Operator> operators = new ArrayList<Operator>();
    private Semaphore callCenterResources;
    private LinkedList<Integer> calls=new LinkedList<Integer>();



    public Dispatcher(Semaphore callCenterResources , List<Operator> operators, List<Supervisor> supervisors, List<Director> directors, LinkedList<Integer> calls){
        this.callCenterResources=callCenterResources;
        this.calls=calls;
        this.directors=directors;
        this.operators=operators;
        this.supervisors=supervisors;
    }

    public void dispatchCall() {
        System.out.println("Starting dispatcher ...");
        this.start();
    }

    @Override
    public void run() {
        try {

            while (true) {

                // Block calls assignation
                mutex.acquire();

                //Any call is waiting?
                if(calls.size()>0){
                    // Take a resource (employee) in order to take assign a call
                    callCenterResources.acquire();

                    // Dispatch call to an available employee with the following order:
                    // first an operator, in the second place a supervisor and as a last option, a director
                    if(!operators.isEmpty() || !supervisors.isEmpty() || !directors.isEmpty()){
                        Integer clientId = calls.remove();

                        System.out.println("Taking call from client  " + clientId);
                        if (!operators.isEmpty()) {
                            Operator operator = operators.remove(0);
                            System.out.println("Assign call from: " + clientId + " to: "+operator.name);

                            operator.start();
                        } else if (!supervisors.isEmpty()) {
                            Supervisor supervisor = supervisors.remove(0);
                            System.out.println("Assign call from: " + clientId + " to: "+supervisor.name);
                            supervisor.start();
                        } else if(!directors.isEmpty()){
                            Director director = directors.remove(0);
                            System.out.println("Assign call from: " + clientId + " to: "+director.name);

                            director.start();
                        }

                    }else {

                        //Any employee was assigned so the resource was released
                        callCenterResources.release();
                    }
                }

                mutex.release();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
