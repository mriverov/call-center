
import dispatcher.CallCenter;
import dispatcher.Client;
import dispatcher.Dispatcher;
import model.impl.Director;
import model.impl.Operator;
import model.impl.Supervisor;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrivero on 21/3/17.
 */
public class CallCenterTest {

    @Test
    public void testDispatch10CallsOk(){
        CallCenter callCenter = new CallCenter(10);

        List<Director> directors = new ArrayList<Director>();
        List<Supervisor> supervisors = new ArrayList<Supervisor>();
        List<Operator> operators = new ArrayList<Operator>();

        directors.add(new Director("Anna",callCenter.getCallCenterResources(), directors));
        directors.add(new Director("Juan",callCenter.getCallCenterResources(), directors));
        directors.add(new Director("Karina",callCenter.getCallCenterResources(), directors));
        directors.add(new Director("Camila",callCenter.getCallCenterResources(), directors));

        supervisors.add(new Supervisor("Maria", callCenter.getCallCenterResources(), supervisors));
        supervisors.add(new Supervisor("Pedro", callCenter.getCallCenterResources(), supervisors));


        operators.add(new Operator("Daniel", callCenter.getCallCenterResources(), operators));
        operators.add(new Operator("Laura", callCenter.getCallCenterResources(), operators));
        operators.add(new Operator("Cristian", callCenter.getCallCenterResources(), operators));
        operators.add(new Operator("Manuel", callCenter.getCallCenterResources(), operators));


        callCenter.setDirectors(directors);
        callCenter.setOperators(operators);
        callCenter.setSupervisors(supervisors);

        Dispatcher dispatcher = new Dispatcher(callCenter.getCallCenterResources(), operators, supervisors, directors, callCenter.getCalls());


        dispatcher.dispatchCall();
        Assert.assertEquals(callCenter.getCallCenterResources().availablePermits(), 10);


        for (int i = 0; i < 14; i++) {
            Client client = new Client(i, callCenter.getCalls());
            client.start();
        }

        // the available resouce on the semaphore has to be 0. 10 calls are taken at the same time.
        Assert.assertEquals(callCenter.getCallCenterResources().availablePermits(), 0);
    }

    @Test
    public void testOnlyOperatorsTakeCalls(){
        CallCenter callCenter = new CallCenter(2);

        List<Director> directors = new ArrayList<Director>();
        List<Supervisor> supervisors = new ArrayList<Supervisor>();
        List<Operator> operators = new ArrayList<Operator>();

        directors.add(new Director("Anna",callCenter.getCallCenterResources(), directors));

        supervisors.add(new Supervisor("Maria", callCenter.getCallCenterResources(), supervisors));

        operators.add(new Operator("Cristian", callCenter.getCallCenterResources(), operators));
        operators.add(new Operator("Manuel", callCenter.getCallCenterResources(), operators));
        operators.add(new Operator("Lucas", callCenter.getCallCenterResources(), operators));

        callCenter.setDirectors(directors);
        callCenter.setOperators(operators);
        callCenter.setSupervisors(supervisors);

        Dispatcher dispatcher = new Dispatcher(callCenter.getCallCenterResources(), operators, supervisors, directors, callCenter.getCalls());

        dispatcher.dispatchCall();

        Assert.assertEquals(callCenter.getCallCenterResources().availablePermits(), 2);

        for (int i = 0; i < 2; i++) {
            Client client = new Client(i, callCenter.getCalls());
            client.start();
        }

        // calls were not assigned to directors or supervisors
        Assert.assertEquals(callCenter.getDirectors().size(), 1);
        Assert.assertEquals(callCenter.getSupervisors().size(), 1);
    }

    @Test
    public void testClientsWaitingWhenNoAvailableOperators(){
        CallCenter callCenter = new CallCenter(2);

        List<Director> directors = new ArrayList<Director>();
        List<Supervisor> supervisors = new ArrayList<Supervisor>();
        List<Operator> operators = new ArrayList<Operator>();

        operators.add(new Operator("Cristian", callCenter.getCallCenterResources(), operators));

        callCenter.setDirectors(directors);
        callCenter.setOperators(operators);
        callCenter.setSupervisors(supervisors);

        Dispatcher dispatcher = new Dispatcher(callCenter.getCallCenterResources(), operators, supervisors, directors, callCenter.getCalls());
        dispatcher.dispatchCall();

        Assert.assertEquals(callCenter.getCallCenterResources().availablePermits(), 2);

        for (int i = 0; i < 5; i++) {
            Client client = new Client(i, callCenter.getCalls());
            client.start();
        }

        Assert.assertTrue(callCenter.getCalls().size()>0);

    }

}
