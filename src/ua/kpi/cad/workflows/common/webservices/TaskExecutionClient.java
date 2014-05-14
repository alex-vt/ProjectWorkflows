package ua.kpi.cad.workflows.common.webservices;

import ua.kpi.cad.workflows.common.taskdata.TaskData;

/**
 * Workflows task execution web client
 */
public interface TaskExecutionClient {
    public TaskData executeTask(TaskData task, String serverAddress, int timeoutSeconds)
            throws WorkflowWebServiceException;
}
