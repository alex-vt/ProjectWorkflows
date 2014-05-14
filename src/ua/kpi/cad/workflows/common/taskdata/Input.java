package ua.kpi.cad.workflows.common.taskdata;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Entity
@Table(name="input")
@XmlRootElement(name="input")
@XmlAccessorType(XmlAccessType.FIELD)
public class Input implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="input_id")
    @XmlTransient
    private int inputId;

    @Column(name="from_data")
    @XmlAttribute(name="from_data", required=true)
    private int fromData;

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

    public Input() { }

    public Input(int fromData, int toUnit, int toPort, String type_, String name) {
        setFromData(fromData);
        setToUnit(toUnit);
        setToPort(toPort);
        setType_(type_);
        setName(name);
    }

    public Input(Input other) {
        setFromData(other.fromData);
        setToUnit(other.toUnit);
        setToPort(other.toPort);
        setType_(other.type_);
        setName(other.name);
    }

    public int getInputId() { return inputId; }
    public void setInputId(int inputId) { this.inputId = inputId; }

    public int getFromData() { return fromData; }
    public void setFromData(int fromData) { this.fromData = fromData; }

    public int getToUnit() { return toUnit; }
    public void setToUnit(int toUnit) { this.toUnit = toUnit; }

    public int getToPort() { return toPort; }
    public void setToPort(int toPort) { this.toPort = toPort; }

    public String getType_() { return type_; }
    public void setType_(String type_) { this.type_ = "" + type_; }

    public String getName() { return name; }
    public void setName(String name) { this.name = "" + name; }
}