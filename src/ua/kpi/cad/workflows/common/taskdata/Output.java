package ua.kpi.cad.workflows.common.taskdata;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Entity
@Table(name="output")
@XmlRootElement(name="output")
@XmlAccessorType(XmlAccessType.FIELD)
public class Output implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="output_id")
    @XmlTransient
    private int outputId;

    @Column(name="from_unit")
    @XmlAttribute(name="from_unit", required=true)
    private int fromUnit;

    @Column(name="from_port")
    @XmlAttribute(name="from_port", required=true)
    private int fromPort;

    @Column(name="to_data")
    @XmlAttribute(name="to_data", required=true)
    private int toData;

    @Column(name="type")
    @XmlAttribute(name="type", required=true)
    private String type_;

    @Column(name="name")
    @XmlAttribute(name="name", required=true)
    private String name;

    public Output() { }

    public Output(int fromUnit, int fromPort, int toData, String type_, String name) {
        setFromUnit(fromUnit);
        setFromPort(fromPort);
        setToData(toData);
        setType_(type_);
        setName(name);
    }

    public Output(Output other) {
        setFromUnit(other.fromUnit);
        setFromPort(other.fromPort);
        setToData(other.toData);
        setType_(other.type_);
        setName(other.name);
    }

    public int getOutputId() { return outputId; }
    public void setOutputId(int outputId) { this.outputId = outputId; }

    public int getFromUnit() { return fromUnit; }
    public void setFromUnit(int fromUnit) { this.fromUnit = fromUnit; }

    public int getFromPort() { return fromPort; }
    public void setFromPort(int fromPort) { this.fromPort = fromPort; }

    public int getToData() { return toData; }
    public void setToData(int toData) { this.toData = toData; }

    public String getType_() { return type_; }
    public void setType_(String type_) { this.type_ = "" + type_; }

    public String getName() { return name; }
    public void setName(String name) { this.name = "" + name; }
}