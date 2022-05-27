package scuttlr.application.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    private HBox titleHBox;
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

//    public ColumnController()
//    {
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.taskTitledPanes = new TitledPane[0];
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

    //    @Override
    //    protected void updateItem(Column column, boolean bool)
    //    {
    //        System.out.println("TEST");
    //        super.updateItem(column, bool);
    //
    //        if (column != null)
    //        {
    //            URL location = ColumnController.class.getResource("src/main/resources/scuttlr/application/display/column.fxml");
    //
    //            FXMLLoader fxmlLoader = new FXMLLoader();
    //            fxmlLoader.setLocation(location);
    //            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
    //
    //            try
    //            {
    //                VBox vBox = (VBox) fxmlLoader.load(location.openStream());
    //                ColumnController controller = fxmlLoader.getController();
    //                controller.setColumn(column);
    //                setGraphic(vBox);
    //            }
    //            catch (IOException e)
    //            {
    //                throw new IllegalStateException(e);
    //            }
    //        }
    //    }

    public void setColumn(Column column)
    {
        this.column = column;
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

    public void newTask(ActionEvent actionEvent)
    {
        //        this.column.addTask();
        //        updateTasks();
        System.out.println(this.column.getTitle());
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

    public void updateTitle(ActionEvent actionEvent)
    {
        this.column.setTitle(this.titleTextField.getText());
    }
}
