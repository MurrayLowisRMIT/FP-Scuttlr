package scuttlr.application.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable
{
    private String name;
    private String description;
    private LocalDate dueDate;
    private int taskID;
    private boolean complete;
    private boolean expanded;

    public Task(int taskID)
    {
        this.name = "New task";
        // TODO default start date to current time
        this.taskID = taskID;
        this.complete = false;
        this.expanded = true;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return this.description;
    }

    public LocalDate getDueDate()
    {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate)
    {
        this.dueDate = dueDate;
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

    public boolean getExpanded()
    {
        return this.expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
        // TODO set completionDate to now or null if unchecking
    }
}
