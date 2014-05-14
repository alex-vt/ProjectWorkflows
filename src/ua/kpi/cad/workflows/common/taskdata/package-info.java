@XmlSchema(
        namespace = "http://cad.kpi.ua",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(namespaceURI = "http://cad.kpi.ua", prefix = "")
        }
)
package ua.kpi.cad.workflows.common.taskdata;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;