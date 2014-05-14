package ua.kpi.cad.workflows.common.interpreter;

/**
 * Interpretation model unit
 */
interface Unit {

    public boolean canRunNow();

    public void run();
}
