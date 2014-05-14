package ua.kpi.cad.workflows.common.datamanagement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import ua.kpi.cad.workflows.common.taskdata.TaskData;

/**
 * Object-XML Mapping (OXM) class with static methods.
 * Transforms data between Java Objects (TaskData) and XML documents (<task_data></task_data>).
 * Data being processed is Task Data for Project Workflows (back-end for workflow execution web service)
 */
public class ObjectXml {
    private static final String XML_STRING_FORMAT = "UTF-8";

    /**
     * Returns XML document <task_data>...</task_data> of TaskData object.
     * Uses JAXB.
     *
     * @param taskData TaskData object to be transformed into XML document
     * @return the text of XML document <task_data>...</task_data>
     */
    public static String getXmlStringFromTaskData(TaskData taskData)
            throws WorkflowDataException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TaskData.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(taskData, outputStream);
        } catch (Exception exception) {
            throw new WorkflowDataException("TaskData to XML conversion exception", exception);
        }

        return outputStream.toString();
    }

    /**
     * Returns TaskData object made by its XML document <task_data>...</task_data>.
     * Uses JAXB.
     *
     * @param xmlString the text of XML document <task_data>...</task_data> to be transformed into TaskData object
     * @return TaskData object
     */
    public static TaskData getTaskDataFromXmlString(String xmlString)
            throws WorkflowDataException {
        TaskData taskData = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TaskData.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes(XML_STRING_FORMAT));
            taskData = (TaskData) unmarshaller.unmarshal(inputStream);
        } catch (Exception exception) {
            throw new WorkflowDataException("XML to TaskData conversion exception", exception);
        }

        return new TaskData(taskData);
    }
}
