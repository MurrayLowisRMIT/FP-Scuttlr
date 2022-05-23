package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import scuttlr.application.model.Column;
import scuttlr.application.model.Task;

import java.io.IOException;
import java.util.LinkedList;

import static scuttlr.application.Main.boardController;

public class ColumnController extends ListCell<Column>
{
    @FXML
    private Pane pane;
    @FXML
    private VBox columnVBox;
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
    private ListView<Task> tasks;

    public ColumnController()
    {
        super();
        this.columnVBox = new VBox();
        this.titleTextField = new TextField("New column");
        this.toolBar = new ToolBar();
        this.moveLeftButton = new Button("<<");
        this.deleteColumnButton = new Button("Delete column");
        this.newTaskButton = new Button("New task");
        this.moveRightButton = new Button(">>");
        // this.tasks = new ListView<>();

        this.toolBar.getItems().addAll(this.moveLeftButton, this.deleteColumnButton, this.newTaskButton, this.moveRightButton);
        // this.columnVBox.getChildren().addAll(this.titleLabel, this.toolBar, this.tasks);
        this.columnVBox.getChildren().addAll(this.titleTextField, this.toolBar);
    }

    @Override
    protected void updateItem(Column column, boolean empty)
    {
        super.updateItem(column, empty);
        if (column != null && !empty) // <== test for null item and empty parameter
        {
            titleTextField.setText(column.getTitle());
            setGraphic(columnVBox);
        }
        else
        {
            setGraphic(null);
        }
    }

    public void deleteColumn(ActionEvent actionEvent)
    {
        System.out.println("ASD");
        boardController.deleteColumn(actionEvent);
    }

    public void newTask(ActionEvent actionEvent)
    {
    }

    public void updateColumnName(InputMethodEvent inputMethodEvent)
    {
    }
}
