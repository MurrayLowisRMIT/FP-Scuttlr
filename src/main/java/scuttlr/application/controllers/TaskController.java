package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import scuttlr.application.model.Task;

import java.util.Date;

public class TaskController
{
    private Task task;
    @FXML
    private TextArea taskDescriptionTextArea;
    @FXML
    private ImageView deleteTaskImageView;
    @FXML
    private Button moveLeftButton;
    @FXML
    private Button moveRightButton;
    @FXML
    private CheckBox completeCheckBox;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Label taskWarningLabel;

    public void setTask(Task task)
    {
        this.task = task;
    }

    public void deleteTask(ActionEvent actionEvent)
    {

    }

    public void moveToLeftColumn(ActionEvent actionEvent)
    {
    }

    public void moveToRightColumn(ActionEvent actionEvent)
    {
    }

    public void checkComplete()
    {
        this.task.setComplete(!this.task.getComplete());
    }

    public void setDueDate()
    {
        this.task.setDueDate(new Date(this.dueDatePicker.getValue().toEpochDay()));
    }

    public void setDueDateWarning()
    {
    }
}
