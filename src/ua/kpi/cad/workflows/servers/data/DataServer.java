package ua.kpi.cad.workflows.servers.data;

import ua.kpi.cad.workflows.common.SettingsManager;
import ua.kpi.cad.workflows.common.datamanagement.ObjectRelational;
import ua.kpi.cad.workflows.common.datamanagement.WorkflowDataException;
import ua.kpi.cad.workflows.common.taskdata.TaskData;
import ua.kpi.cad.workflows.common.webservices.JaxWsClient;
import ua.kpi.cad.workflows.common.webservices.TaskExecutionClient;
import ua.kpi.cad.workflows.common.webservices.WorkflowWebServiceException;

import org.h2.tools.Server;
import java.sql.SQLException;
import java.util.List;

/**
 * Workflows tasks user data server
 */
public class DataServer {
    private static final String SETTINGS_PATH = "settings/data_server_settings.xml";

    public static final String EXECUTION_SERVER_ADDRESS = SettingsManager.getSettingValue(SETTINGS_PATH,
            "execution_server_address");
    public static final int EXECUTION_TIMEOUT_SECONDS = Integer.parseInt(SettingsManager.getSettingValue(SETTINGS_PATH,
            "execution_timeout_seconds"));
    public static final int DATABASE_POLL_INTERVAL_MILLISECONDS
            = Integer.parseInt(SettingsManager.getSettingValue(SETTINGS_PATH, "database_poll_interval_milliseconds"));
    public static final String DATABASE_PORT = SettingsManager.getSettingValue(SETTINGS_PATH, "database_port");
    public static final String DATABASE_URL = SettingsManager.getSettingValue(SETTINGS_PATH, "database_url");

    public static void main(String[] args) {
        initializeObjectRelationalSubsystem();
        startDatabaseServer();

        for (;;) {
            System.out.println("Started processing new tasks");
            try {
                List<TaskData> taskDataList = ObjectRelational.getNewTaskDataFromDatabase("task");
                System.out.println("Received tasks: " + taskDataList.size());
                for (TaskData taskData: taskDataList) {
                    System.out.println("Start execution of task " + taskData.getName());
                    TaskExecutionClient jaxWsClient = new JaxWsClient();
                    TaskData result = jaxWsClient.executeTask(taskData, EXECUTION_SERVER_ADDRESS,
                            EXECUTION_TIMEOUT_SECONDS);
                    ObjectRelational.putTaskDataToDatabase(result);
                    System.out.println("Finished execution of task " + taskData.getName());
                }
            } catch (WorkflowDataException e) {
                e.printStackTrace();
            } catch (WorkflowWebServiceException e) {
                e.printStackTrace();
            }
            System.out.println("Finished processing new tasks for " + DATABASE_POLL_INTERVAL_MILLISECONDS + " ms");
            try {
                Thread.sleep(DATABASE_POLL_INTERVAL_MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initializeObjectRelationalSubsystem() {
        try {
            ObjectRelational.initialize(DATABASE_URL, "", "");
        } catch (WorkflowDataException e) {
            e.printStackTrace();
            System.out.println("Critical malfunction of Object-Relational subsystem. Exiting...");
            System.exit(1);
        }
    }

    private static void startDatabaseServer() {
        try {
            final String[] args = new String[] {"-tcpPort", DATABASE_PORT, "-tcpAllowOthers"};
            Server server = Server.createTcpServer(args);
            server.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
