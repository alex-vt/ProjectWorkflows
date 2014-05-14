package ua.kpi.cad.workflows.common.interpreter;

/**
 * Conditional unit implementation in the task interpretation model
 */
class UnitConditional implements Unit {

    private Signal inputData1;
    private Signal inputData2;
    private Signal outputControl1;
    private Signal outputControl2;

    private String condition;

    public UnitConditional(Signal inputData1, Signal inputData2, Signal outputControl1, Signal outputControl2,
                           String condition) {
        this.inputData1 = inputData1;
        this.inputData2 = inputData2;
        this.outputControl1 = outputControl1;
        this.outputControl2 = outputControl2;

        this.condition = condition;
    }

    @Override
    public boolean canRunNow() {
        synchronized (inputData2) {
            return inputData1.isActive() && inputData2.isActive();
        }
    }

    @Override
    public void run() {
        if (canRunNow()) {
            if (/* condition */ true) {
                activateOutputControl(outputControl1);
            } else {
                activateOutputControl(outputControl2);
            }
        }
    }

    public void activateOutputControl(Signal outputControl) {
        outputControl.activate();
    }
}
