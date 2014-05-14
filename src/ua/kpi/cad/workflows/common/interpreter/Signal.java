package ua.kpi.cad.workflows.common.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Interpretation model signal
 */
class Signal {
    private boolean isActive = false;
    private String fileName = "";
    private List<Signal> relatedSignalsList;

    private int unitIdFromTask;
    private int portIdFromTask;

    public Signal(int unitIdFromTask, int portIdFromTask) {
        relatedSignalsList = new ArrayList<Signal>();
        setUnitIdFromTask(unitIdFromTask);
        setPortIdFromTask(portIdFromTask);
    }

    public void setUnitIdFromTask(int id) {
        unitIdFromTask = id;
    }

    public int getUnitIdFromTask() {
        return unitIdFromTask;
    }

    public void setPortIdFromTask(int id) {
        portIdFromTask = id;
    }

    public int getPortIdFromTask() {
        return portIdFromTask;
    }

    public void addConnectedSignal(Signal signal) {
        signal.setFileName(getFileName());
        relatedSignalsList.add(signal);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean relatedSignalsExist() {
        if (relatedSignalsList.size() > 0) {
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

    public void propagateToRelatedSignals() {
        for (Signal connectedSignal: relatedSignalsList) {
            connectedSignal.activate();
            connectedSignal.setFileName(fileName);
        }
        deactivate();
    }
}
