package ua.kpi.cad.workflows.common.interpreter;

/**
 * Workflows task interpreting exception
 */
public class WorkflowInterpretingException extends Exception {
    public WorkflowInterpretingException(String message, Exception exception) {
        super(message + ".\nLower level cause: " + exception.getMessage());
        this.setStackTrace(exception.getStackTrace());
    }
}