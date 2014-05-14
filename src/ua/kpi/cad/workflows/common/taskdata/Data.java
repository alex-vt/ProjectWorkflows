package ua.kpi.cad.workflows.common.taskdata;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Entity
@Table(name="data")
@XmlRootElement(name="data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Data implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="data_id")
    @XmlTransient
    private int dataId;

    @Column(name="id")
    @XmlAttribute(name="id", required=true)
    private int id;

    @Column(name="availability")
    @XmlAttribute(name="availability", required=true)
    private String availability;

    @Column(name="type")
    @XmlAttribute(name="type", required=true)
    private String type_;

    @Column(name="name")
    @XmlElement(name="name", required=true)
    private String name;

    @Column(name="content")
    @XmlElement(name="content", required=true)
    private String content;

    public Data() { }

    public Data(int id, String availability, String type_, String name, String content) {
        setId(id);
        setAvailability(availability);
        setType_(type_);
        setName(name);
        setContent(content);
    }

    public Data(Data other) {
        setId(other.id);
        setAvailability(other.availability);
        setType_(other.type_);
        setName(other.name);
        setContent(other.content);
    }

    public int getDataId() { return dataId; }
    public void setDataId(int globalId) { this.dataId = globalId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAvailability() { return availability; }
    public void setAvailability(String format) { this.availability = "" + format; }

    public String getType_() { return type_; }
    public void setType_(String type_) { this.type_ = "" + type_; }

    public String getName() { return name; }
    public void setName(String name) { this.name = "" + name; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = "" + content; }
}

