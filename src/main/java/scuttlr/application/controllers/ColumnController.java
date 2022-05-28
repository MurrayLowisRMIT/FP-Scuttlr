package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import scuttlr.application.model.Column;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ColumnController extends ListCell<Column> implements Initializable
{
    private Column column;
    @FXML
    private TextField titleTextField;
    @FXML
    private HBox titleHBox;
    @FXML
    private ToolBar toolBar;
    @FXML
    private ImageView deleteColumnImageView;
    @FXML
    private Button moveLeftButton;
    @FXML
    private Button newTaskButton;
    @FXML
    private Button moveRightButton;
    @FXML
    private VBox tasksVBox;
    @FXML
    private ListView<Pane> tasksListView;
    @FXML
    private LinkedList<Pane> taskPanes;
    private LinkedList<TaskController> taskControllers;
    private BoardController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.taskPanes = new LinkedList<>();
        this.taskControllers = new LinkedList<>();
    }

    // load tasks and refresh tasks displays
    public void loadTasks()
    {
        for (int i = 0; i < this.column.getTasks().size(); i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/task.fxml"));
            try
            {
                this.taskPanes.add(loader.load());
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            // add handlers to list
            this.taskControllers.add(loader.getController());
            this.taskControllers.getLast().setTask(this.column.getTasks().get(i), this);
        }
        this.tasksListView.getItems().addAll(this.taskPanes);
    }

    public void newColumn(Column column, BoardController parentController)
    {
        this.column = column;
        this.parentController = parentController;
        this.titleTextField.setText(column.getTitle());
        loadTasks();
    }

    public void deleteColumn()
    {
        this.parentController.deleteColumn(this.column.getColumnID());
    }

    public void newTask()
    {
        this.column.addTask();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/task.fxml"));
        try
        {
            this.taskPanes.add(loader.load());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        // add handler to list
        this.taskControllers.add(loader.getController());
        this.taskControllers.getLast().setTask(this.column.getTasks().getLast(), this);
        this.tasksListView.getItems().add(this.taskPanes.getLast());
    }

    public void updateTitle()
    {
        this.column.setTitle(this.titleTextField.getText());
    }

    public void moveColumnRight()
    {
        this.parentController.moveColumn(this.column.getColumnID(), +1);
    }

    public void moveColumnLeft()
    {
        this.parentController.moveColumn(this.column.getColumnID(), -1);
    }

    // swap tasks within column
    public void moveTask(int taskID, int direction)
    {
        if (taskID + direction >= 0 && taskID + direction < this.column.getTasks().size())
        {
            Collections.swap(this.column.getTasks(), taskID, taskID + direction);

            // swap elements
            this.column.reorderTasks();
            Collections.swap(this.taskPanes, taskID, taskID + direction);
            Collections.swap(this.taskControllers, taskID, taskID + direction);
            this.tasksListView.getItems().clear();
            this.tasksListView.getItems().addAll(this.taskPanes);
        }
        else
        {
            this.parentController.setWarning("Can't move any further");
        }
    }

    public void deleteTask(int taskID)
    {
        this.taskControllers.remove(taskID);
        this.taskPanes.remove(taskID);
        this.tasksListView.getItems().remove(taskID);
        this.column.removeTask(taskID);
    }
}
