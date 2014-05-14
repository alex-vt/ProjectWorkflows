package ua.kpi.cad.workflows.common.webservices;

/**
 * Workflows web service data exchange exception
 */
public class WorkflowWebServiceException extends Exception {
    public WorkflowWebServiceException(String message, Exception exception) {
        super(message + ".\nLower level cause: " + exception.getMessage());
        this.setStackTrace(exception.getStackTrace());
    }
}