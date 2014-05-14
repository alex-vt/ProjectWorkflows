package ua.kpi.cad.workflows.common.webservices;

import ua.kpi.cad.workflows.common.taskdata.TaskData;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

/**
 * Web Services based (JAX-WS) implementation of the Workflows task execution web client
 */
public class JaxWsClient implements TaskExecutionClient {
    private final String namespaceUri = "http://webservices.common.workflows.cad.kpi.ua/";
    private final String serviceName = "WorkflowsExecutionServer";
    private final String wsdlSuffix = "?wsdl";

    @Override
    public TaskData executeTask(TaskData task, String serverAddress, int timeoutSeconds)
            throws WorkflowWebServiceException {
        JaxWsServerEndpoint endpoint = getEndpoint(serverAddress);
        TaskData result = endpoint.executeTask(task, timeoutSeconds);
        if (result == null) {
            throw new WorkflowWebServiceException("JAX-WS web service returned empty result task", new Exception());
        }
        return result;
    }

    private JaxWsServerEndpoint getEndpoint(String serverAddress)
            throws WorkflowWebServiceException {
        URL url = null;
        try {
            url = new URL(serverAddress + wsdlSuffix);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        QName qname = new QName(namespaceUri, serviceName);

        Service service = null;
        try {
            service = Service.create(url, qname);
        } catch (WebServiceException exception) {
            throw new WorkflowWebServiceException("JAX-WS web service error", exception);
        }

        JaxWsServerEndpoint endpoint = service.getPort(JaxWsServerEndpoint.class);
        return endpoint;
    }
}
