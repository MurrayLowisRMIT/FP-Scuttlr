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
import javafx.scene.layout.Pane;
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
    private Pane pane;
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
    private TitledPane[] taskTitledPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.column = new Column();
        this.tasks = FXCollections.observableArrayList();

        this.tasks.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                System.out.println("THING HAPPENED");
            }
        });
    }

    //    public ColumnController()
    //    {
    //        super();
    //    }
    //
    //    @Override
    //    protected void updateItem(Column column, boolean empty)
    //    {
    //        super.updateItem(column, empty);
    //        if (column != null && !empty)
    //        {
    //            titleTextField.setText(column.getTitle());
    //            setGraphic(taskTitledPane);
    //        }
    //        else
    //        {
    //            setGraphic(null);
    //        }
    //    }

    public void deleteColumn(ActionEvent actionEvent)
    {
        boardController.deleteColumn(actionEvent);
    }

    public void newTask(ActionEvent actionEvent)
    {
        this.column.addTask();
        updateTasks();
    }

    public void updateTasks()
    {
        // TODO dynamically populate instead of fully deleting and rebuilding every time
        tasksVBox.getChildren().clear();
        //        this.taskTitledPane = new TitledPane[this.column.getTasks().size()];
        //        for (int i = 0; i < this.column.getTasks().size(); i++)
        this.taskTitledPane = new TitledPane[5];
        for (int i = 0; i < 5; i++)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/task.fxml"));
            try
            {
                this.taskTitledPane[i] = loader.load();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        this.tasksVBox.getChildren().addAll(this.taskTitledPane);
    }

    public void updateColumnName(InputMethodEvent inputMethodEvent)
    {
    }
}
