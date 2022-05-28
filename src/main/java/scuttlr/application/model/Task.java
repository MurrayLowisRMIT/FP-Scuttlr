package scuttlr.application.model;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable
{
    private String name;
    private String description;
    private Date dueDate;
    private Date completionDate;
    private int taskID;
    private boolean complete;

    public Task(int taskID)
    {
        this.name = "New task";
        this.description = "description";
        // TODO default start date to current time
        this.dueDate = new Date();
        this.completionDate = new Date();
        this.taskID = taskID;
        this.complete = false;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDueDate()
    {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate)
    {
        this.dueDate = dueDate;
    }

    public Date getCompletionDate()
    {
        return this.completionDate;
    }

    public void setCompletionDate(Date completionDate)
    {
        this.completionDate = completionDate;
    }

    public int getTaskID()
    {
        return this.taskID;
    }

    public void setTaskID(int taskID)
    {
        this.taskID = taskID;
    }

    public boolean getComplete()
    {
        return this.complete;
    }

    public void setComplete(boolean complete)
    {
        this.complete = complete;
        // TODO set completionDate to now or null if unchecking
    }
}
