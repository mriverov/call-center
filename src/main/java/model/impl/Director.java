package model.impl;

import model.Employee;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by mrivero on 19/3/17.
 */
public class Director extends Thread implements Employee {

    public String name;
    private Semaphore callCenterResources;
    private List<Director> employees;

    public Director(String name, Semaphore callCenterResources, List<Director> operators){
        this.name = name;
        this.callCenterResources = callCenterResources;
        this.employees = operators;
    }

    public void takeCall() {
        System.out.println("Director " + this.name + " taking call");
    }

    public void endCall() {
        System.out.println("Director " + this.name + " end call");
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
            this.employees.add(new Director(this.name, this.callCenterResources, this.employees));
        }
    }
}
