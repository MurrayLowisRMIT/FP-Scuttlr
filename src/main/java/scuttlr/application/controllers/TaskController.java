package scuttlr.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import scuttlr.application.model.Task;

import java.time.LocalDate;

public class TaskController
{
    private Task task;
    @FXML
    private TitledPane taskTitledPane;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private TextArea taskDescriptionTextArea;
    @FXML
    private CheckBox completeCheckBox;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Label taskWarningLabel;
    private ColumnController parentController;

    public void setTask(Task task, ColumnController parentController)
    {
        this.task = task;
        this.parentController = parentController;
        this.taskNameTextField.setText(task.getName());
        this.taskDescriptionTextArea.setText(task.getDescription());
        this.completeCheckBox.setSelected(task.getComplete());
        this.taskTitledPane.setExpanded(task.getExpanded());
        if (task.getDueDate() != null)
        {
            this.dueDatePicker.setValue(LocalDate.from(task.getDueDate()));
            // TODO set due date warning
        }
    }

    public void deleteTask()
    {
        this.parentController.deleteTask(this.task.getTaskID());
    }

    public void moveUp()
    {
        this.parentController.moveTask(this.task.getTaskID(), -1);
    }

    public void moveDown()
    {
        this.parentController.moveTask(this.task.getTaskID(), +1);
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

    public void toggleExpanded()
    {
        this.task.setExpanded(!this.task.getExpanded());
    }

    public void setDueDate()
    {
        this.task.setDueDate(this.dueDatePicker.getValue());
    }
}
