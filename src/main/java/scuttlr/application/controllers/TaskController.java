package scuttlr.application.controllers;

import javafx.scene.control.ListCell;
import scuttlr.application.model.Task;
import scuttlr.application.model.Column;

import java.util.ArrayList;

public class TaskController extends ListCell<Column>
{
    private ArrayList<Task> tasks;

    public TaskController()
    {
        this.tasks = new ArrayList<Task>();
    }

    @Override
    protected void updateItem(Column column, boolean empty)
    {
        super.updateItem(column, empty);
        if (column != null && !empty) // <== test for null item and empty parameter
        {
//            this.name = column.getTasks().;
//            price.setText(String.format("%d $", column.getPrice()));
//            setGraphic(content);
        }
        else
        {
            setGraphic(null);
        }
    }
}
