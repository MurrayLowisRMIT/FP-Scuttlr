package scuttlr.application.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Column implements Serializable
{
    private String title;
    private LinkedList<Task> tasks;

    public Column()
    {
        this.title = "New column";
        this.tasks = new LinkedList<>();
    }

    public Column(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public LinkedList<Task> getTasks()
    {
        return this.tasks;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void addTask()
    {
        this.tasks.add(new Task());
    }

    public void removeTask(Task task)
    {
        //TODO confirm this works
        this.tasks.remove(task);
    }
}
