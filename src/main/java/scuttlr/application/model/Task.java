package scuttlr.application.model;

import javafx.scene.control.ListCell;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable
{
    private String name;
    private String description;
    private Date start;
    private Date due;
    private Date completion;

    public void setTask()
    {
        this.name = "New task";
        this.description = "description";
        // TODO default start date to now
        this.start = new Date();
        this.due = new Date();
    }
}
