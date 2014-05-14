package ua.kpi.cad.workflows.common.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic unit implementation in the task interpretation model
 */
class UnitLogic implements Unit {
    //
    private List<Signal> inputSignalList;
    private Signal outputSignal;

    private String type;

    public UnitLogic(String type) {
        inputSignalList = new ArrayList<Signal>();
        this.type = type;
    }

    @Override
    public boolean canRunNow() {
        if (type.startsWith("AND")) {
            for (Signal inputSignal: inputSignalList) {
                if (! inputSignal.isActive()) {
                    return false;
                }
            }
            return true;
        } else /* OR */ {
            for (Signal inputSignal: inputSignalList) {
                if (inputSignal.isActive()) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void run() {
        if (canRunNow()) {
            if (type.equals("AND")) {
                // If only one data item found, perform data output. If no data found, perform control output.
                boolean dataFound = false;
                for (Signal inputSignal: inputSignalList) {
                    if (inputSignal.getFileName().length() > 0) {
                        activateOutputSignal(inputSignal);
                        dataFound = true;
                        break;
                    }
                }
                if (! dataFound) {
                    activateOutputSignal(inputSignalList.get(0));
                }
            } else /* OR */ {
                // Output first (and, the only) signal
                for (Signal inputSignal: inputSignalList) {
                    if (inputSignal.isActive()) {
                        activateOutputSignal(inputSignal);
                        break;
                    }
                }
            }
        }
    }

    public void addInputSignal(Signal inputSignal) {
        inputSignalList.add(inputSignal);
    }

    public void setOutputSignal (Signal outputSignal) {
        this.outputSignal = outputSignal;
    }

    public void activateOutputSignal(Signal inputSignalSource) {
        outputSignal.setFileName(inputSignalSource.getFileName());
        outputSignal.activate();
    }
}
