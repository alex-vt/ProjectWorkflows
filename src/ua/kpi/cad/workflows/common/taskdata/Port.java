package ua.kpi.cad.workflows.common.taskdata;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Entity
@Table(name="port")
@XmlRootElement(name="port")
@XmlAccessorType(XmlAccessType.FIELD)
public class Port implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="port_id")
    @XmlTransient
    private int portId;

    @Column(name="id")
    @XmlAttribute(name="id", required=true)
    private int id;

    @Column(name="type")
    @XmlAttribute(name="type", required=true)
    private String type_;

    @Column(name="name")
    @XmlAttribute(name="name", required=true)
    private String name;

    @Column(name="direction")
    @XmlAttribute(name="direction", required=true)
    private String direction;

    public Port() { }

    public Port(int id, String type_, String name, String direction) {
        setId(id);
        setType_(type_);
        setName(name);
        setDirection(direction);
    }

    public Port(Port other) {
        setId(other.id);
        setType_(other.type_);
        setName(other.name);
        setDirection(other.direction);
    }

    public int getPortId() { return portId; }
    public void setPortId(int portId) { this.portId = portId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType_() { return type_; }
    public void setType_(String type_) { this.type_ = "" + type_; }

    public String getName() { return name; }
    public void setName(String name) { this.name = "" + name; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = "" + direction; }
}