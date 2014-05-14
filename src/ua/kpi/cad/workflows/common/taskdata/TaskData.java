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
@Table(name="task_data")
@XmlRootElement(name="task_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="task_data_id")
    @XmlTransient
    private int taskDataId;

    @Column(name="type")
    @XmlAttribute(name="type", required=true)
    private String type_;

    @Column(name="name")
    @XmlAttribute(name="name", required=true)
    private String name;

    @Column(name="workflow_name")
    @XmlAttribute(name="workflow_name", required=true)
    private String workflowName;

    @Column(name="revision")
    @XmlAttribute(name="revision", required=true)
    private String revision;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JoinColumn(name="task_data_id",  referencedColumnName="task_data_id")
    @XmlElement(name="data")
    private List<Data> dataList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JoinColumn(name="task_data_id",  referencedColumnName="task_data_id")
    @XmlElement(name="unit", required=true)
    private List<Unit> unitList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JoinColumn(name="task_data_id", referencedColumnName="task_data_id")
    @XmlElement(name="input", required=true)
    private List<Input> inputList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JoinColumn(name="task_data_id", referencedColumnName="task_data_id")
    @XmlElement(name="link")
    private List<Link> linkList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @JoinColumn(name="task_data_id", referencedColumnName="task_data_id")
    @XmlElement(name="output", required=true)
    private List<Output> outputList;

    public TaskData() { }

    public TaskData(String type_, String name, String workflowName, String revision, List<Data> dataList,
                    List<Unit> unitList, List<Input> inputList, List<Link> linkList, List<Output> outputList) {
        setType_(type_);
        setName(name);
        setWorkflowName(workflowName);
        setRevision(revision);
        setData(dataList);
        setUnit(unitList);
        setInput(inputList);
        setLink(linkList);
        setOutput(outputList);
    }

    public TaskData(TaskData other) {
        setType_(other.type_);
        setName(other.name);
        setWorkflowName(other.workflowName);
        setRevision(other.revision);
        setData(other.dataList);
        setUnit(other.unitList);
        setInput(other.inputList);
        setLink(other.linkList);
        setOutput(other.outputList);
    }

    public int getTaskDataId() { return taskDataId; }
    public void setTaskDataId(int taskDataId) { this.taskDataId = taskDataId; }

    public String getType_() { return type_; }
    public void setType_(String type_) { this.type_ = "" + type_; }

    public String getName() { return name; }
    public void setName(String name) { this.name = "" + name; }

    public String getWorkflowName() { return workflowName; }
    public void setWorkflowName(String workflowName) { this.workflowName = "" + workflowName; }

    public String getRevision() { return revision; }
    public void setRevision(String revision) { this.revision = "" + revision; }

    public List<Data> getData() { return dataList; }
    public void setData(List<Data> dataList) { this.dataList = new ArrayList<Data>(); if (dataList != null)
        for (int i = 0; i < dataList.size(); ++i) { this.dataList.add(new Data(dataList.get(i))); } }

    public List<Unit> getUnit() { return unitList; }
    public void setUnit(List<Unit> unitList) { this.unitList = new ArrayList<Unit>(); if (unitList != null)
        for (int i = 0; i < unitList.size(); ++i) { this.unitList.add(new Unit(unitList.get(i))); } }

    public List<Input> getInput() { return inputList; }
    public void setInput(List<Input> inputList) { this.inputList = new ArrayList<Input>(); if (inputList != null)
        for (int i = 0; i < inputList.size(); ++i) { this.inputList.add(new Input(inputList.get(i))); } }

    public List<Link> getLink() { return linkList; }
    public void setLink(List<Link> linkList) { this.linkList = new ArrayList<Link>(); if (linkList != null)
        for (int i = 0; i < linkList.size(); ++i) { this.linkList.add(new Link(linkList.get(i))); } }

    public List<Output> getOutput() { return outputList; }
    public void setOutput(List<Output> outputList) { this.outputList = new ArrayList<Output>(); if (outputList != null)
        for (int i = 0; i < outputList.size(); ++i) { this.outputList.add(new Output(outputList.get(i))); } }
}

