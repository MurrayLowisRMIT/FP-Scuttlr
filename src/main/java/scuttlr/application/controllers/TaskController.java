package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ListCell;
import scuttlr.application.model.Task;
import scuttlr.application.model.Column;

import java.util.ArrayList;

public class TaskController
{
    private Task task;

    public void setTask(Task task)
    {
        this.task = task;
    }

    public void deleteTask(ActionEvent actionEvent)
    {

    }
}
