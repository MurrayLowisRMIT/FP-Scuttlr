package scuttlr.application.model;

import java.util.LinkedHashSet;

public class Column
{
    private String title;
    private LinkedHashSet<Task> tasks;

    public Column(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public LinkedHashSet<Task> getTasks()
    {
        return this.tasks;
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
