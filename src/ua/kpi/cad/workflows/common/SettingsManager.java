package ua.kpi.cad.workflows.common;

import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * XML-formatted settings extractor
 */
public class SettingsManager {
    public static String getSettingValue(String settingsFilePath, String settingXmlNodeName) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            InputSource inputSource = new InputSource(settingsFilePath);
            return xpath.evaluate("//" + settingXmlNodeName, inputSource);
        } catch(XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
