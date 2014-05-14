package ua.kpi.cad.workflows.tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Common helper functions
 */
public class HelperTools {

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
