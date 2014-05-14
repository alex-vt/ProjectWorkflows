package ua.kpi.cad.workflows.common.interpreter;

import ua.kpi.cad.workflows.common.taskdata.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Task interpreter
 */
public class Interpreter {
    private List<Unit> unitList = new ArrayList<Unit>();
    private List<Signal> signalList = new ArrayList<Signal>();
    private List<Signal> outputSignalList = new ArrayList<Signal>();

    private int timeoutSeconds = 5;
    private int executionCyclesCount = 0;
    private int executionCyclesMax = 1000;

    public static void setExecutionFolder(String folder) {
        FileInputOutput.setExecutionFolder(folder);
    }

    public static void setProgramsFolder(String folder) {
        FileInputOutput.setProgramsFolder(folder);
    }

    public TaskData execute(TaskData task, int timeoutSeconds)
            throws WorkflowInterpretingException {
        this.timeoutSeconds = timeoutSeconds;
        TaskData result = execute(task);
        return result;
    }

    public TaskData execute(TaskData task)
            throws WorkflowInterpretingException {

        String executionSubfolderName = "exec_" + System.currentTimeMillis();
        FileInputOutput.createExecutionSubfolder(executionSubfolderName);

        buildExecutableModelFromTask(task, executionSubfolderName);
        connectDependableSignals(task);
        setOutputSignals(task);

        while (! areAllOutputSignalsActive(outputSignalList)) {
            controlExecutionCycle();
            // propagate active signals from outputs of units to inputs of other units
            for (Signal signal: signalList) {
                if (signal.isActive() && signal.relatedSignalsExist()) {
                    signal.propagateToRelatedSignals();
                }
            }
            // propagate active signals inside units
            for (Unit unit: unitList) {
                if (unit.canRunNow()) {
                    unit.run();
                }
            }
            controlProgramUnits();
        }

        TaskData result = attachOutputData(task, executionSubfolderName);
        return result;
    }

    private void controlExecutionCycle()
            throws WorkflowInterpretingException {
        ++executionCyclesCount;
        if (executionCyclesCount > executionCyclesMax) {
            throw new WorkflowInterpretingException("Task execution cycles overfill (>" + executionCyclesMax + ")",
                    new Exception());
        }
    }

    private void controlProgramUnits()
            throws WorkflowInterpretingException {
        for (Unit unit: unitList) {
            if (unit instanceof UnitProgram) {
                if (((UnitProgram)unit).hasException())
                    throw ((UnitProgram)unit).getException();
            }
        }
    }

    private TaskData attachOutputData(TaskData task, String executionSubfolderName)
            throws WorkflowInterpretingException {
        TaskData result = new TaskData(task);
        result.setType_("result");
        for (Output outputFromTask: result.getOutput()) {
            int outputSignalId = getSignalIdByUnitAndPortId(outputFromTask.getToData(), -1);
            Data outputData = new Data(outputFromTask.getToData(), "local", outputFromTask.getType_(),
                    outputFromTask.getName(),
                    FileInputOutput.readFileFromProgramOutput(signalList.get(outputSignalId).getFileName(),
                            executionSubfolderName));
            List<Data> allData = result.getData();
            allData.add(outputData);
            result.setData(allData);
        }
        return  result;
    }

    private void connectDependableSignals(TaskData task)
            throws WorkflowInterpretingException {
         // by inputs
        for (Input inputFromTask: task.getInput()) {
            int sourceSignalId = getSignalIdByUnitAndPortId(inputFromTask.getFromData(), -1);
            int destinationSignalId = getSignalIdByUnitAndPortId(inputFromTask.getToUnit(), inputFromTask.getToPort());
            signalList.get(sourceSignalId).addConnectedSignal(signalList.get(destinationSignalId));
        }
         // by links, if exist
        if (task.getLink() != null) {
            for (Link linkFromTask: task.getLink()) {
                int sourceSignalId = getSignalIdByUnitAndPortId(linkFromTask.getFromUnit(), linkFromTask.getFromPort());
                int destinationSignalId = getSignalIdByUnitAndPortId(linkFromTask.getToUnit(), linkFromTask.getToPort());
                signalList.get(sourceSignalId).addConnectedSignal(signalList.get(destinationSignalId));
            }
        }
    }

    private int getSignalIdByUnitAndPortId(int unitIdFromTask, int portIdFromTask)
            throws WorkflowInterpretingException {
        boolean found = false;
        for (int i = 0; i < signalList.size(); ++i) {
            Signal signal = signalList.get(i);
            if (signal.getUnitIdFromTask() == unitIdFromTask && signal.getPortIdFromTask() == portIdFromTask) {
                found = true;
                return i;
            }
        }
        return -1;
    }

    private void setOutputSignals(TaskData task)
            throws WorkflowInterpretingException {
        for (Output outputFromTask: task.getOutput()) {
            int outputSignalId = getSignalIdByUnitAndPortId(outputFromTask.getFromUnit(), outputFromTask.getFromPort());
            signalList.get(outputSignalId).setFileName(outputFromTask.getName());
            signalList.get(outputSignalId).setUnitIdFromTask(outputFromTask.getToData());
            signalList.get(outputSignalId).setPortIdFromTask(-1);
            outputSignalList.add(signalList.get(outputSignalId));
        }
    }

    private void buildExecutableModelFromTask(TaskData task, String executionSubfolderName)
            throws WorkflowInterpretingException {
        // get signals from task inputs
        for (Data dataFromTask: task.getData()) {
            Signal signalToAdd = new Signal(dataFromTask.getId(), -1);
            if (dataFromTask.getType_().equals("control")) {
                signalToAdd.setFileName("");
            } else {
                signalToAdd.setFileName(dataFromTask.getName());
                FileInputOutput.writeFileForProgramInput(signalToAdd.getFileName(), executionSubfolderName,
                        dataFromTask.getContent());
            }
            signalToAdd.activate();
            signalList.add(signalToAdd);
        }

        // get units and signals from task units
        for (ua.kpi.cad.workflows.common.taskdata.Unit unitFromTask: task.getUnit()) {
            if (unitFromTask.getType_().startsWith("AND")) {

            } else if (unitFromTask.getType_().startsWith("OR")) {

            } else if (unitFromTask.getType_().startsWith("CONDITIONAL")) {

            } else /* Program unit */ {
                addProgramUnitWithAllSignals(unitFromTask, executionSubfolderName);
            }
        }
    }

    private void addProgramUnitWithAllSignals(ua.kpi.cad.workflows.common.taskdata.Unit unitFromTask,
                                              String executionSubfolderName)
            throws WorkflowInterpretingException {
        Unit unitToAdd = new UnitProgram(unitFromTask.getType_(), executionSubfolderName);
        for (Port port: unitFromTask.getPort()) {
            Signal signalToAdd = new Signal(unitFromTask.getId(), port.getId());
            if (port.getType_().equals("control")) {
                signalToAdd.setFileName("");
            } else {
                signalToAdd.setFileName("unit " + unitFromTask.getId() + " port " + port.getId());
            }
            if (port.getDirection().equals("in")) {
                ((UnitProgram)unitToAdd).addMandatoryInputSignal(port.getName(), signalToAdd);
            } else if (port.getDirection().equals("in_optional")) {
                ((UnitProgram)unitToAdd).addOptionalInputSignal(port.getName(), signalToAdd);
            } else /* out */ {
                ((UnitProgram)unitToAdd).addOutputSignal(port.getName(), signalToAdd);
            }
            signalList.add(signalToAdd);
        }
        unitList.add(unitToAdd);
    }

    private boolean areAllOutputSignalsActive(List<Signal> outputSignalList) {
        for (Signal outputSignal: outputSignalList) {
            if (! outputSignal.isActive()) {
                return false;
            }
        }
        return true;
    }
}
