package ua.kpi.cad.workflows.common.interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * External program unit implementation in the task interpretation model
 */
class UnitProgram implements Unit {

    private List<Signal> inputSignalList;
    private List<Signal> outputSignalList;

    private List<String> mandatoryInputPortNames;
    private List<String> optionalInputPortNames;
    private List<String> outputPortNames;

    private String programType;
    private String executionSubfolderName;
    private volatile boolean isProgramRunning;
    private WorkflowInterpretingException programRuntimeException;

    public UnitProgram(String programType, String executionSubfolderName) {
        inputSignalList = new ArrayList<Signal>();
        outputSignalList = new ArrayList<Signal>();

        mandatoryInputPortNames = new ArrayList<String>();
        optionalInputPortNames = new ArrayList<String>();
        outputPortNames = new ArrayList<String>();

        this.programType = programType;
        this.executionSubfolderName = executionSubfolderName;
    }

    @Override
    public boolean canRunNow() {
        for (int i = 0; i < mandatoryInputPortNames.size(); ++i) {
            if (! inputSignalList.get(i).isActive()) {
                return false;
            }
        }
        if (isProgramRunning) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        if (canRunNow()) {
            String trialLaunchCommand = "";
            try {
                trialLaunchCommand = getLaunchCommand();
            } catch (WorkflowInterpretingException e) {
                storeException(e);
            }
            for (Signal inputSignal: inputSignalList) {
                inputSignal.deactivate();
            }
            final String launchCommand = trialLaunchCommand;
            Runnable runnableProgram = new Runnable() {
                @Override
                public void run() {
                    isProgramRunning = true;
                    try {
                        Process programProcess = Runtime.getRuntime().exec("" + launchCommand);
                        Thread.sleep(500);// replace!
                    } catch (IOException e) {
                        storeException(new WorkflowInterpretingException("For program unit, command "
                                + launchCommand + " failed to start", e));
                    } catch (IllegalArgumentException e) {
                        storeException(new WorkflowInterpretingException("For program unit, command "
                                + launchCommand + " failed to start", e));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activateOutputSignals();
                    isProgramRunning = false;
                }
            };
            runnableProgram.run();
        }
    }

    private void storeException(WorkflowInterpretingException exception) {
        programRuntimeException = exception;
    }

    public boolean hasException() {
        if (programRuntimeException == null) {
            return false;
        }
        return true;
    }

    public WorkflowInterpretingException getException() {
        return programRuntimeException;
    }

    private void activateOutputSignals() {
        for (Signal signal: outputSignalList) {
            signal.activate();
        }
    }

    public void addMandatoryInputSignal(String name, Signal inputSignal) {
        mandatoryInputPortNames.add(name);
        inputSignalList.add(inputSignal); // attention
    }

    public void addOptionalInputSignal(String name, Signal inputSignal) {
        optionalInputPortNames.add(name);
        inputSignalList.add(inputSignal);
    }

    public void addOutputSignal(String name, Signal outputSignal) {
        outputPortNames.add(name);
        outputSignalList.add(outputSignal);
    }

    private String getLaunchCommand()
            throws WorkflowInterpretingException {
        String programExtension = FileInputOutput.getProgramExtension(programType);
        String launchCommand = FileInputOutput.getProgramCallEssentials(programType, programExtension);
        // Mandatory active arguments first
        for (int i = 0; i < mandatoryInputPortNames.size(); ++i) {
            if (inputSignalList.get(i).isActive()) {
                launchCommand += FileInputOutput.getFullArgument(mandatoryInputPortNames.get(i),
                        inputSignalList.get(i).getFileName(), executionSubfolderName);
            }
        }
        // Then optional active
        for (int i = 0; i < optionalInputPortNames.size(); ++i) {
            if (inputSignalList.get(i + mandatoryInputPortNames.size()).isActive()) {
                launchCommand += FileInputOutput.getFullArgument(optionalInputPortNames.get(i),
                        inputSignalList.get(i + mandatoryInputPortNames.size()).getFileName(), executionSubfolderName);
            }
        }
        // Then output
        for (int i = 0; i < outputPortNames.size(); ++i) {
            launchCommand += FileInputOutput.getFullArgument(outputPortNames.get(i),
                    outputSignalList.get(i).getFileName(), executionSubfolderName);
        }
        return  launchCommand;
    }
}
