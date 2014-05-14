package ua.kpi.cad.workflows.common.webservices;

import javax.xml.ws.Endpoint;

/**
 * Web Services based (JAX-WS) implementation of the Workflows task execution web server
 */
public class JaxWsServer implements TaskExecutionServer {

    @Override
    public void start(String address) {
        System.out.println("Starting Server");
        JaxWsServerEndpointImpl soapServer = new JaxWsServerEndpointImpl();

        Endpoint.publish(address, soapServer);

        System.out.println("Server is running");

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void finish() {
    }
}
