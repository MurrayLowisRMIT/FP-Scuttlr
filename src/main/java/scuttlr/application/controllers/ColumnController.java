package scuttlr.application.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.VBox;
import scuttlr.application.model.Column;
import scuttlr.application.model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static scuttlr.application.Main.boardController;

public class ColumnController extends ListCell<Column> implements Initializable
{
    private Column column;
    @FXML
    private TextField titleTextField;
    @FXML
    private ToolBar toolBar;
    @FXML
    private Button moveLeftButton;
    @FXML
    private Button deleteColumnButton;
    @FXML
    private Button newTaskButton;
    @FXML
    private Button moveRightButton;
    @FXML
    private VBox tasksVBox;
    @FXML
    protected ObservableList<Task> tasks;
    @FXML
    private TitledPane[] taskTitledPanes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.taskTitledPanes = new TitledPane[0];
        this.tasks = FXCollections.observableArrayList();
        this.column = boardController.addColumnController(this);

        this.tasks.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                System.out.println("THING HAPPENED");
            }
        });
    }

    public void deleteColumn()
    {
        boardController.deleteColumn(this.column.getColumnID());
    }

    public void newTask(ActionEvent actionEvent)
    {
        this.column.addTask();
        updateTasks();
    }

    public void deleteTask(ActionEvent actionEvent)
    {

    }

    public void updateTasks()
    {
        // TODO dynamically populate instead of fully deleting and rebuilding every time
        tasksVBox.getChildren().clear();
        this.taskTitledPanes = new TitledPane[this.column.getTasks().size()];
        for (int i = 0; i < this.column.getTasks().size(); i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/task.fxml"));
            try
            {
                this.taskTitledPanes[i] = loader.load();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        this.tasksVBox.getChildren().addAll(this.taskTitledPanes);
    }

    public void updateColumnName(InputMethodEvent inputMethodEvent)
    {
    }
}
