package scuttlr.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import scuttlr.application.model.Board;
import scuttlr.application.model.TaskList;
import scuttlr.application.model.Task;

import java.util.LinkedHashSet;

public class TaskListController
{
    @FXML
    private Label titleLabel;
    @FXML
    private LinkedHashSet<Task> tasks;

    public TaskListController(Board board)
    {
        this.titleLabel.setText(board.getBoardName());
    }

    public void defineList(TaskList list)
    {
    }

    public void createNewTask()
    {

    }
}
