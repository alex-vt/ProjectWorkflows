package ua.kpi.cad.workflows.common.taskdata;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Entity
@Table(name="link")
@XmlRootElement(name="link")
@XmlAccessorType(XmlAccessType.FIELD)
public class Link implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="link_id")
    @XmlTransient
    private int linkId;

    @Column(name="from_unit")
    @XmlAttribute(name="from_unit", required=true)
    private int fromUnit;

    @Column(name="from_port")
    @XmlAttribute(name="from_port", required=true)
    private int fromPort;

    @Column(name="to_unit")
    @XmlAttribute(name="to_unit", required=true)
    private int toUnit;

    @Column(name="to_port")
    @XmlAttribute(name="to_port", required=true)
    private int toPort;

    @Column(name="type")
    @XmlAttribute(name="type", required=true)
    private String type_;

    @Column(name="name")
    @XmlAttribute(name="name", required=true)
    private String name;

    public Link() { }

    public Link(int fromUnit, int fromPort, int toUnit, int toPort, String type_, String name) {
        setFromUnit(fromUnit);
        setFromPort(fromPort);
        setToUnit(toUnit);
        setToPort(toPort);
        setType_(type_);
        setName(name);
    }

    public Link(Link other) {
        setFromUnit(other.fromUnit);
        setFromPort(other.fromPort);
        setToUnit(other.toUnit);
        setToPort(other.toPort);
        setType_(other.type_);
        setName(other.name);
    }

    public int getLinkId() { return linkId; }
    public void setLinkId(int linkId) { this.linkId = linkId; }

    public int getFromUnit() { return fromUnit; }
    public void setFromUnit(int fromUnit) { this.fromUnit = fromUnit; }

    public int getFromPort() { return fromPort; }
    public void setFromPort(int fromPort) { this.fromPort = fromPort; }

    public int getToUnit() { return toUnit; }
    public void setToUnit(int toUnit) { this.toUnit = toUnit; }

    public int getToPort() { return toPort; }
    public void setToPort(int toPort) { this.toPort = toPort; }

    public String getType_() { return type_; }
    public void setType_(String type_) { this.type_ = "" + type_; }

    public String getName() { return name; }
    public void setName(String name) { this.name = "" + name; }
}