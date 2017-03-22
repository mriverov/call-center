package dispacher;

import model.impl.Director;
import model.impl.Operator;
import model.impl.Supervisor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by mrivero on 19/3/17.
 */
public class CallCenter {

    private final int maxConcurrentCalls;
    private final Semaphore callCenterResources;

    private List<Director> directors = new ArrayList<Director>();
    private List<Supervisor> supervisors = new ArrayList<Supervisor>();
    private List<Operator> operators = new ArrayList<Operator>();

    private LinkedList<Integer> calls=new LinkedList<Integer>();

    public CallCenter(int cantConcurrentCalls){
        this.maxConcurrentCalls=cantConcurrentCalls;
        this.callCenterResources = new Semaphore(maxConcurrentCalls, true);
    }

    private void initCallCenter() {
        directors.add(new Director("Anna",callCenterResources, directors));
        directors.add(new Director("Juan",callCenterResources, directors));
        directors.add(new Director("Karina",callCenterResources, directors));
        directors.add(new Director("Camila",callCenterResources, directors));

        supervisors.add(new Supervisor("Maria", callCenterResources, supervisors));
        supervisors.add(new Supervisor("Pedro", callCenterResources, supervisors));


        operators.add(new Operator("Daniel", callCenterResources, operators));
        operators.add(new Operator("Laura", callCenterResources, operators));
        operators.add(new Operator("Cristian", callCenterResources, operators));

        Dispatcher dispatcher = new Dispatcher(callCenterResources, operators, supervisors, directors, calls);
        dispatcher.dispatchCall();

        for (int i = 0; i < 10; i++) {
            Client client = new Client(i, calls);
            client.start();
        }

    }

    public List<Director> getDirectors() {
        return directors;
    }

    public List<Supervisor> getSupervisors() {
        return supervisors;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public void setSupervisors(List<Supervisor> supervisors) {
        this.supervisors = supervisors;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    public Semaphore getCallCenterResources() {
        return callCenterResources;
    }

    public LinkedList<Integer> getCalls() {
        return calls;
    }


    public static void main(String[] args) {
        // Just one way to be executed
        CallCenter callCenter = new CallCenter(8);
        callCenter.initCallCenter();
    }

}
