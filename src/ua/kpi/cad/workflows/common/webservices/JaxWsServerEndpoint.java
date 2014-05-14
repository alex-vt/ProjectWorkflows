package ua.kpi.cad.workflows.common.webservices;

import ua.kpi.cad.workflows.common.taskdata.TaskData;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * JAX-WS endpoint of the Workflows task execution server
 */
@WebService
public interface JaxWsServerEndpoint {
    @WebMethod
    public TaskData executeTask(TaskData task, int timeoutSeconds);
}
