package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import scuttlr.application.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class TaskController
{
    private Task task;
    @FXML
    private TextField taskNameTextField;
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
    private TaskController parentController;

    public void setTask(Task task)
    {
        this.task = task;
        this.taskNameTextField.setText(task.getName());
        this.completeCheckBox.setSelected(task.getComplete());
        if (task.getDueDate() != null)
        {
            this.dueDatePicker.setValue(LocalDate.from(task.getDueDate()));
        }
    }

    public void setParentController(TaskController taskController)
    {
        this.parentController = taskController;
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

    public void updateName()
    {
        this.task.setName(this.taskNameTextField.getText());
    }

    public void setDescription()
    {
        this.task.setDescription(this.taskDescriptionTextArea.getText());
    }

    public void checkComplete()
    {
        this.task.setComplete(!this.task.getComplete());
    }

    public void setDueDate()
    {
        this.task.setDueDate(this.dueDatePicker.getValue());
    }

    public void setDueDateWarning()
    {
    }
}
