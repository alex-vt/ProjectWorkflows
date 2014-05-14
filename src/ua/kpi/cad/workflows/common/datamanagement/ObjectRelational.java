package ua.kpi.cad.workflows.common.datamanagement;

import ua.kpi.cad.workflows.common.taskdata.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object-Relational Mapping (ORM) class with static methods.
 * Transforms data between Java Objects (TaskData) and the relational database.
 * Data being processed is Task Data for Project Workflows (back-end for workflow execution web service)
 */
public class ObjectRelational {
    private static boolean isInitialized = false;
    private static EntityManagerFactory entityManagerFactory = null;

    /**
     * Gets TaskData objects list from database (of specified type), using JPA mapping.
     *
     * Also deletes the corresponding records from data_exchange table
     * (see putNewTaskDataToDatabase)
     * Requires existing tables mapped with TaskData entities by JPA.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     *
     * @param typeName Task Data type
     * @return list of new TaskData objects got from database
     */
    public static List<TaskData> getNewTaskDataFromDatabase(String typeName)
            throws WorkflowDataException {
        EntityManager entityManager = getTransactionEntityManager();

        List taskDataList = entityManager.createNativeQuery(
                "SELECT * FROM task_data WHERE task_data_id IN ("
                + "SELECT task_data_id FROM data_exchange WHERE type = '" + typeName + "');",
                TaskData.class ).getResultList();

        finishTransaction(entityManager);

        flushDataExchangeEntries(typeName);
        return taskDataList;
    }

    /**
     * Puts one TaskData object to database, using JPA mapping.
     *
     * Also puts the corresponding record into data_exchange table (TaskData id and type).
     *      This table is required to store temporal TaskData ids
     *      to be requested from database later by another application
     *      which this database is shared with, for example:
     *          Back End application (this) reads "task" and writes "result" / "special" entities,
     *          Front End application reads "result" / "special" and writes "task" entities.
     *
     * If mapped tables do not exist, this method creates them.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     *
     * @param taskData TaskData object to be put into database
     */
    public static void putTaskDataToDatabase(TaskData taskData)
            throws WorkflowDataException {
        EntityManager entityManager = getTransactionEntityManager();

        TaskData taskDataToPersist = new TaskData(taskData);
        entityManager.persist(taskDataToPersist);

        // Also putting record into data_exchange table.
        entityManager.createNativeQuery(
            "INSERT INTO data_exchange VALUES ("
                    + taskDataToPersist.getTaskDataId() + ", "
                    + "'" + taskDataToPersist.getType_() + "'"
                    + ");"
        ).executeUpdate();

        finishTransaction(entityManager);
    }

    /**
     * Returns text representation of a table in database.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     *
     * @param tableName name of table in database
     * @return the text representation of a table
     */
    public static String getStringTableFromDatabase(String tableName)
            throws WorkflowDataException {
        String rawColumnsNames = getTableSchemaFromDatabase(tableName);

        EntityManager entityManager = getTransactionEntityManager();

        final int COLUMN_WIDTH = 14;
        String tableColumnsNames = getTableColumnsNamesLine(rawColumnsNames, tableName, COLUMN_WIDTH);

        List queryResultContent = entityManager.createNativeQuery(
                "SELECT * FROM " + tableName + ""
        ).getResultList();
        String tableContentText = "";
        for (Object tableRows: queryResultContent) {
            tableContentText += "\n";
            for (Object rowItem: (Object[])tableRows) {
                if (rowItem != null) {
                    tableContentText
                            += String.format("%-" + (COLUMN_WIDTH + 1) + "." + COLUMN_WIDTH + "s", rowItem.toString());
                }
            }
        }

        queryResultContent = entityManager.createNativeQuery(
                "SELECT * FROM " + tableName + " t"
        ).getResultList();

        finishTransaction(entityManager);
        return "> table " + tableName.toUpperCase() + ":\n" + tableColumnsNames + tableContentText + "\n";
    }

    /**
     * Returns text representation of the schema of the table in database.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     *
     * @param tableName name of table in database
     * @return the text representation of a table creation info (schema)
     */
    public static String getTableSchemaFromDatabase(String tableName)
            throws WorkflowDataException {
        EntityManager entityManager = getTransactionEntityManager();

        List queryResultContent;
        queryResultContent = entityManager.createNativeQuery(
                "SELECT * FROM INFORMATION_SCHEMA.TABLES"
        ).getResultList();
        String rawColumnsNames = "";
        for (Object tableProperties: queryResultContent) {
            if (((Object[])tableProperties)[2].toString().equalsIgnoreCase(tableName)) {
                rawColumnsNames = ((Object[])tableProperties)[5].toString();
                break;
            }
        }
        finishTransaction(entityManager);
        return rawColumnsNames + "\n";
    }

    /**
     * Creates missing tables in database, filling them only with basic entries,
     * and removes all "null"-word-type TaskData created using default constructor.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     */
    public static void initialize(String databaseUrl, String username, String password)
            throws WorkflowDataException {
        if (! isInitialized) {
            Map<String, String> databaseConnectionProperties = new HashMap<String, String>();
            databaseConnectionProperties.put("javax.persistence.jdbc.url", databaseUrl);
            databaseConnectionProperties.put("javax.persistence.jdbc.user", username);
            databaseConnectionProperties.put("javax.persistence.jdbc.password", password);
            entityManagerFactory = Persistence.createEntityManagerFactory("TaskData", databaseConnectionProperties);
            isInitialized = true;
        }
        tryCreateDataExchangeTableInDatabase();
        putTaskDataToDatabase(new TaskData()); // type will be a word "null"
        removeTypedTaskDataFromDatabase("null");
        flushDataExchangeEntries("null");
    }

    /**
     * Returns current state of this Object-Relational Mapping class
     *
     * @return initialized / not-initialized state
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Clears data_exchange tables which stores temporal information (for specified type) related
     * to data exchange between applications by this database (see putTaskDataToDatabase)
     *
     * @param typeName Task Data type
     */
    public static void flushDataExchangeEntries(String typeName)
            throws WorkflowDataException {
        EntityManager entityManager = getTransactionEntityManager();

        entityManager.createNativeQuery(
                "DELETE FROM data_exchange WHERE type = '" + typeName + "' ;"
        ).executeUpdate();

        finishTransaction(entityManager);
    }

    /**
     * Removes all TaskData entries from database which have given type
     * (can be "task" / "result" / "special" / "test")
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     *
     * @param typeName name of type for TaskData to be deleted
     */
    public static void removeTypedTaskDataFromDatabase(String typeName)
            throws WorkflowDataException {
        EntityManager entityManager = getTransactionEntityManager();

        List queryResultContent = entityManager.createNativeQuery(
                "SELECT task_data_id FROM task_data WHERE type = '" + typeName + "' ;"
        ).getResultList();

        for (int i = 0; i < queryResultContent.size(); ++i) {
            String numberString = queryResultContent.get(i).toString();
            int value = Integer.parseInt(numberString);
            removeTaskDataFromDatabaseById(value);
        }

        finishTransaction(entityManager);
    }

    /**
     * Removes a TaskData entry from database which has specified task_data_id.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     */
    private static void removeTaskDataFromDatabaseById(int taskDataId)
            throws WorkflowDataException {
        EntityManager entityManager = getTransactionEntityManager();

        TaskData taskDataToRemove = entityManager.find(TaskData.class, taskDataId);
        entityManager.remove(taskDataToRemove);

        finishTransaction(entityManager);
    }

    /**
     * Starts the JPA transaction for database and returns its Entity Manager.
     * Before this, checks if ORM is initialized.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     * Entity Manager is used for persisting and getting objects, and for other queries.
     *
     * @return JPA Entity Manager which maintains the current database transaction
     */
    private static EntityManager getTransactionEntityManager()
            throws WorkflowDataException {
        if (! isInitialized) {
            throw new WorkflowDataException("Object-Relational Mapping is not initialized", new Exception());
        }
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
        } catch (Exception exception) {
            throw new WorkflowDataException("Connection to database failed", exception);
        }
        entityManager.getTransaction().begin();
        return entityManager;
    }

    /**
     * Commits the JPA transaction for database.
     * Database is maintained by JPA according to settings in src/META-INF/persistence.xml
     *
     * @param entityManager JPA Entity Manager which maintains the current database transaction
     */
    private static void finishTransaction(EntityManager entityManager) {
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * Creates (if not exist) a special data_exchange table for exchange TaskData entities
     * between applications (see putTaskDataToDatabase).
     * This table is not mapped by JPA in src/META-INF/persistence.xml
     *
     * The data_exchange table is relationally-independent and has columns:
     *      task_data_id - stores TaskData id,
     *      type - stores TaskData type.
     *
     * This method does NOT affect tables mapped by JPA.
     * This method does NOT affect ANY existing data in database.
     */
    private static void tryCreateDataExchangeTableInDatabase()
            throws WorkflowDataException {
        EntityManager entityManager = getTransactionEntityManager();

        entityManager.createNativeQuery(
                "CREATE TABLE IF NOT EXISTS data_exchange ("
                        + "task_data_id INTEGER NOT NULL, "
                        + "type VARCHAR, "
                        + "PRIMARY KEY (task_data_id));"
        ).executeUpdate();

        finishTransaction(entityManager);
    }

    /**
     * Returns a formatted String line with table columns names for given table creation SQL query and table name.
     * By parsing table creation query metadata of H2 database.
     *
     * @param rawColumnsNames table creation SQL query
     * @param tableName name of table which columns names are returned
     * @param COLUMN_WIDTH place for formatted column name
     * @return String with table columns names, formatted by width of value COLUMN_WIDTH
     */
    private static String getTableColumnsNamesLine(String rawColumnsNames, String tableName, final int COLUMN_WIDTH) {
        String[] rawColumnsNamesLexems = rawColumnsNames.split(
                "[\t\n\r \\(\\),.'\"']|CREATE|CACHED|PUBLIC|"
                + "TABLE|NUMBER|NOT|NULL|VARCHAR|INTEGER|DEFAULT|NEXT|VALUE|_TO_"
        );

        String tableColumnsNames = "";
        for (String columnName: rawColumnsNamesLexems) {
            if (columnName.equalsIgnoreCase("primary")) {
                break;
            }
            if (columnName.length() == 0
                    || columnName.equalsIgnoreCase(tableName)
                    || columnName.startsWith("SYSTEM_SEQUENCE")
                    || columnName.equalsIgnoreCase("SEQUENCE")
                    || columnName.equalsIgnoreCase("INT")
                    || columnName.equalsIgnoreCase("FOR")
                    || (columnName.getBytes())[0] >= 48 && (columnName.getBytes())[0] <= 57
                   ) {
                continue;
            }
            tableColumnsNames += String.format("%-" + (COLUMN_WIDTH + 1) + "." + COLUMN_WIDTH + "s", columnName);
        }

        return tableColumnsNames;
    }
}
