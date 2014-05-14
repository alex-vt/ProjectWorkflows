package ua.kpi.cad.workflows.common.datamanagement;

/**
 * Workflows data transform exception
 */
public class WorkflowDataException extends Exception {
    public WorkflowDataException(String message, Exception exception) {
        super(message + ".\nLower level cause: " + exception.getMessage());
        this.setStackTrace(exception.getStackTrace());
    }
}