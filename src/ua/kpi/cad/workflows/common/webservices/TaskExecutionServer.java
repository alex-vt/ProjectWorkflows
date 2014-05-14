package ua.kpi.cad.workflows.common.webservices;

/**
 * Workflows task execution web server
 */
public interface TaskExecutionServer {
    public void start(String address);
    public boolean isRunning();
    public void finish();
}