package scuttlr.application.controllers;

import javafx.scene.control.ListCell;
import scuttlr.application.model.Task;
import scuttlr.application.model.TaskList;

import java.util.ArrayList;

public class TaskController extends ListCell<TaskList>
{
    private ArrayList<Task> tasks;

    public TaskController()
    {
        this.tasks = new ArrayList<Task>();
    }

    @Override
    protected void updateItem(TaskList taskList, boolean empty)
    {
        super.updateItem(taskList, empty);
        if (taskList != null && !empty) // <== test for null item and empty parameter
        {
//            this.name = taskList.getTasks().;
//            price.setText(String.format("%d $", taskList.getPrice()));
//            setGraphic(content);
        }
        else
        {
            setGraphic(null);
        }
    }
}
