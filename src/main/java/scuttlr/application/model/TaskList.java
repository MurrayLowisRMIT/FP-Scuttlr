package scuttlr.application.model;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class TaskList implements Serializable
{
    private String title;
    private LinkedHashSet<Task> tasks;

    public TaskList()
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public LinkedHashSet<Task> getTasks()
    {
        return tasks;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void addTask(Task task)
    {
        this.tasks.add(task);
    }

    public void removeTask(Task task)
    {
        //TODO confirm this works
        this.tasks.remove(task);
    }
}
