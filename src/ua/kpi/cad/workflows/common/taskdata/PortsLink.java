package ua.kpi.cad.workflows.common.taskdata;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Entity
@Table(name="ports_link")
@XmlRootElement(name="ports_link")
@XmlAccessorType(XmlAccessType.FIELD)
public class PortsLink implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ports_link_id")
    @XmlTransient
    private int portsLinkId;

    @Column(name="in")
    @XmlAttribute(name="in", required=true)
    private int in;

    @Column(name="out")
    @XmlAttribute(name="out", required=true)
    private int out;

    @Column(name="case_expression")
    @XmlAttribute(name="case_expression", required=true)
    private String caseExpression;

    public PortsLink() { }

    public PortsLink(int in, int out, String caseExpression) {
        setIn(in);
        setOut(out);
        setCaseExpression(caseExpression);
    }

    public PortsLink(PortsLink other) {
        setIn(other.in);
        setOut(other.out);
        setCaseExpression(other.caseExpression);
    }

    public int getPortsLinkId() { return portsLinkId; }
    public void setPortsLinkId(int portsLinkId) { this.portsLinkId = portsLinkId; }

    public int getIn() { return in; }
    public void setIn(int in) { this.in = in; }

    public int getOut() { return out; }
    public void setOut(int out) { this.out = out; }

    public String getCaseExpression() { return caseExpression; }
    public void setCaseExpression(String caseExpression) { this.caseExpression = "" + caseExpression; }
}