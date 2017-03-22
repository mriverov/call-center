package model.impl;

import model.Employee;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by mrivero on 19/3/17.
 */
public class Operator extends Thread implements Employee {

    public String name;
    private Semaphore callCenterResources;
    private List<Operator> employees;

    public Operator(String name, Semaphore callCenterResources, List<Operator> operators){
        this.name = name;
        this.callCenterResources = callCenterResources;
        this.employees = operators;
    }


    public void takeCall() {
        System.out.println("Operator " + this.name + " taking call");
    }

    public void endCall() {
        System.out.println("Operator " + this.name + " end call");
    }

    @Override
    public void run() {
            int callTime = (int) (Math.random() * (10 - 5)) + 5;

            this.takeCall();

            try {
                sleep(callTime * 1000);
                this.endCall();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Release Lock
                callCenterResources.release();
                this.employees.add(new Operator(this.name, this.callCenterResources, this.employees));
            }
    }


}

