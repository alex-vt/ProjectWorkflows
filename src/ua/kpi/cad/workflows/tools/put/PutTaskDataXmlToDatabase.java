package ua.kpi.cad.workflows.tools.put;

import ua.kpi.cad.workflows.common.SettingsManager;
import ua.kpi.cad.workflows.common.datamanagement.ObjectRelational;
import ua.kpi.cad.workflows.common.datamanagement.ObjectXml;
import ua.kpi.cad.workflows.common.datamanagement.WorkflowDataException;
import ua.kpi.cad.workflows.common.taskdata.TaskData;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Adds the specified task to database for further execution
 */
public class PutTaskDataXmlToDatabase {
    private static final String SETTINGS_PATH = "settings/data_server_test_settings.xml";

    public static final String XML_TASK_PATH = SettingsManager.getSettingValue(SETTINGS_PATH, "test_task_path");
    public static final String DATABASE_SERVER_ADDRESS
            = SettingsManager.getSettingValue(SETTINGS_PATH, "database_address");

    public static void main(String[] args) {
        initializeObjectRelationalSubsystem();
        String taskXml = loadAndGetXmlDocument(XML_TASK_PATH);
        try {
            TaskData taskData = ObjectXml.getTaskDataFromXmlString(taskXml);
            ObjectRelational.putTaskDataToDatabase(taskData);
        } catch (WorkflowDataException e) {
            e.printStackTrace();
        }
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

    public static String loadAndGetXmlDocument(String xmlDocumentPath) {
        String xmlDocument = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(xmlDocumentPath));
            xmlDocument = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlDocument;
    }
}
