package ua.kpi.cad.workflows.tools.database;

import ua.kpi.cad.workflows.common.SettingsManager;
import ua.kpi.cad.workflows.common.datamanagement.ObjectRelational;
import ua.kpi.cad.workflows.common.datamanagement.WorkflowDataException;

/**
 * Workflows database contents viewer
 */
public class ShowDatabase {
    private static final String SETTINGS_PATH = "settings/data_server_test_settings.xml";
    public static final String DATABASE_SERVER_ADDRESS
            = SettingsManager.getSettingValue(SETTINGS_PATH, "database_address");

    public static void main(String[] args) {
        initializeObjectRelationalSubsystem();
        try {
            System.out.println(ObjectRelational.getStringTableFromDatabase("data_exchange"));
            System.out.println(ObjectRelational.getStringTableFromDatabase("task_data"));
            System.out.println(ObjectRelational.getStringTableFromDatabase("data"));
            System.out.println(ObjectRelational.getStringTableFromDatabase("unit"));
            System.out.println(ObjectRelational.getStringTableFromDatabase("port"));
            System.out.println(ObjectRelational.getStringTableFromDatabase("input"));
            System.out.println(ObjectRelational.getStringTableFromDatabase("link"));
            System.out.println(ObjectRelational.getStringTableFromDatabase("output"));
        } catch (WorkflowDataException e) {e.printStackTrace(); }
    }

    public static void initializeObjectRelationalSubsystem() {
        try {
            ObjectRelational.initialize(DATABASE_SERVER_ADDRESS, "", "");
        } catch (WorkflowDataException e) {
            e.printStackTrace();
            System.out.println("Critical malfunction of Object-Relational subsystem. Exiting...");
            System.exit(1);
        }
    }
}
