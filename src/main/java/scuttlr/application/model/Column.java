package scuttlr.application.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Column implements Serializable
{
    private String title;
    private LinkedList<Task> tasks;
    private int columnID;

    public Column(int columnID)
    {
        this.title = "New column";
        this.tasks = new LinkedList<>();
        this.columnID = columnID;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getColumnID()
    {
        return this.columnID;
    }

    public void setColumnID(int ID)
    {
        this.columnID = ID;
    }

    public LinkedList<Task> getTasks()
    {
        return this.tasks;
    }

    public void addTask()
    {
        Task task = new Task(this.tasks.size());
        this.tasks.add(task);
    }

    // delete task and update taskIDs
    public void removeTask(int taskID)
    {
        this.tasks.remove(taskID);
        reorderTasks();
    }

    public void reorderTasks()
    {
        for (int i = 0; i < this.tasks.size(); i++)
        {
            this.tasks.get(i).setTaskID(i);
        }
    }
}
