package scuttlr.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import scuttlr.application.model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static scuttlr.application.Main.boardController;

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
    private ListView tasksListView;
    @FXML
    protected ObservableList<Task> tasks;
    @FXML
    private LinkedList<Pane> taskPanes;
    private LinkedList<TaskController> taskControllers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.taskPanes = new LinkedList<>();
        this.tasks = FXCollections.observableArrayList();
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
            // load tasks
            this.taskControllers.add(loader.getController());
            this.taskControllers.getLast().setTask(this.column.getTasks().get(i));
            // give task access to its respective columnController
            this.taskControllers.getLast().setParentController(this.taskControllers.getLast());
        }
        this.tasksListView.getItems().addAll(this.taskPanes);
    }

    public void setColumn(Column column)
    {
        this.column = column;
        this.titleTextField.setText(column.getTitle());
    }

    public void moveColumnRight(ActionEvent event)
    {
        this.column.setColumnID(this.column.getColumnID() + 1);
    }

    public void moveColumnLeft(ActionEvent event)
    {
        this.column.setColumnID(this.column.getColumnID() - 1);
    }

    public void deleteColumn()
    {
        boardController.deleteColumn(this.column.getColumnID());
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
        this.taskControllers.add(loader.getController());
        this.taskControllers.getLast().setTask(this.column.getTasks().getLast());
        this.tasksListView.getItems().add(this.taskPanes.getLast());
    }

    public void deleteTask(ActionEvent actionEvent)
    {

    }

    public void updateTitle()
    {
        this.column.setTitle(this.titleTextField.getText());
    }
}
