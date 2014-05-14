package ua.kpi.cad.workflows.servers.execution;

import ua.kpi.cad.workflows.common.SettingsManager;
import ua.kpi.cad.workflows.common.interpreter.Interpreter;
import ua.kpi.cad.workflows.common.webservices.JaxWsServer;
import ua.kpi.cad.workflows.common.webservices.TaskExecutionServer;

/**
 * Workflows tasks execution server
 */
public class ExecutionServer {
    private static final String SETTINGS_PATH = "settings/execution_server_settings.xml";

    public static void main(String[] args) {
        final String serverAddress = SettingsManager.getSettingValue(SETTINGS_PATH, "server_address");
        Interpreter.setExecutionFolder(SettingsManager.getSettingValue(SETTINGS_PATH, "execution_folder"));
        Interpreter.setProgramsFolder(SettingsManager.getSettingValue(SETTINGS_PATH, "programs_folder"));

        TaskExecutionServer jaxWsServer = new JaxWsServer();
        jaxWsServer.start(serverAddress);
    }
}
