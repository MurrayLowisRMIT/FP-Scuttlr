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

    public Task(String name, String description, Date start, Date due)
    {
        this.name = name;
        this.description = description;
        this.start = start;
        this.due = due;
    }
}
