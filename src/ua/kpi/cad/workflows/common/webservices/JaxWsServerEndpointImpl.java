package ua.kpi.cad.workflows.common.webservices;

import ua.kpi.cad.workflows.common.interpreter.Interpreter;
import ua.kpi.cad.workflows.common.interpreter.WorkflowInterpretingException;
import ua.kpi.cad.workflows.common.taskdata.TaskData;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * JAX-WS endpoint implementation of the Workflows task execution server
 */
@WebService(serviceName = "WorkflowsExecutionServer",
        endpointInterface = "ua.kpi.cad.workflows.common.webservices.JaxWsServerEndpoint")
public class JaxWsServerEndpointImpl {

    @WebMethod
    public TaskData executeTask(TaskData task, int timeoutSeconds) {
        System.out.println("Executing task " + task.getName());

        Interpreter interpreter = new Interpreter();
        TaskData result = null;
        try {
            result = interpreter.execute(task);
        } catch (WorkflowInterpretingException e) {
            e.printStackTrace();
        }

        System.out.println("Task " + task.getName() + " execution finished");
        return result;
    }
}
