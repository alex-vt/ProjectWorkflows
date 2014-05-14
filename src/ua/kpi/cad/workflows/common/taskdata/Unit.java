package ua.kpi.cad.workflows.common.taskdata;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="unit")
@XmlRootElement(name="unit")
@XmlAccessorType(XmlAccessType.FIELD)
public class Unit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="unit_id")
    @XmlTransient
    private int unitId;

    @Column(name="id")
    @XmlAttribute(name="id", required=true)
    private int id;

    @Column(name="type")
    @XmlAttribute(name="type", required=true)
    private String type_;

    @Column(name="name")
    @XmlElement(name="name", required=true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JoinColumn(name="unit_id",  referencedColumnName="unit_id")
    @XmlElement(name="port", required=true)
    private List<Port> portList;

    public Unit() { }

    public Unit(int id, String type_, String name, List<Port> portList, List<PortsLink> portsLinkList) {
        setId(id);
        setType_(type_);
        setName(name);
        setPort(portList);
    }

    public Unit(Unit other) {
        setId(other.id);
        setType_(other.type_);
        setName(other.name);
        setPort(other.portList);
    }

    public int getUnitId() { return unitId; }
    public void setUnitId(int unitId) { this.unitId = unitId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType_() { return type_; }
    public void setType_(String type_) { this.type_ = "" + type_; }

    public String getName() { return name; }
    public void setName(String name) { this.name = "" + name; }

    public List<Port> getPort() { return portList; }
    public void setPort(List<Port> portList) { this.portList = new ArrayList<Port>(); if (portList != null)
        for (int i = 0; i < portList.size(); ++i) { this.portList.add(new Port(portList.get(i))); } }
}

