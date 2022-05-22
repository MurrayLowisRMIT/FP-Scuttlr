package scuttlr.application.model;

import javafx.scene.control.ListCell;

import java.io.Serializable;
import java.util.LinkedHashSet;

public class TaskList extends ListCell<Board>
{
    private String title;
    private LinkedHashSet<Task> tasks;

    public TaskList()
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
