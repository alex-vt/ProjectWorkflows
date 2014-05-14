package ua.kpi.cad.workflows.tools.schema;

import ua.kpi.cad.workflows.common.SettingsManager;
import ua.kpi.cad.workflows.common.datamanagement.ObjectRelational;
import ua.kpi.cad.workflows.common.datamanagement.WorkflowDataException;

/**
 * Workflows database schema viewer
 */
public class ShowDatabaseSchema {
    private static final String SETTINGS_PATH = "settings/data_server_test_settings.xml";
    public static final String databaseServerAddress
            = SettingsManager.getSettingValue(SETTINGS_PATH, "database_address");

    public static void main(String[] args) {
        initializeObjectRelationalSubsystem();
        try {
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("data_exchange"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("task_data"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("data"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("unit"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("port"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("ports_link"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("input"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("link"));
            System.out.println(ObjectRelational.getTableSchemaFromDatabase("output"));
        } catch (WorkflowDataException e) {e.printStackTrace(); }
    }

    public static void initializeObjectRelationalSubsystem() {
        try {
            ObjectRelational.initialize(databaseServerAddress, "", "");
        } catch (WorkflowDataException e) {
            e.printStackTrace();
            System.out.println("Critical malfunction of Object-Relational subsystem. Exiting...");
            System.exit(1);
        }
    }
}
