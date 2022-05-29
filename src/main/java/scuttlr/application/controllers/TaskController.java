package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import scuttlr.application.model.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    @FXML
    private ImageView dueDateWarningImageView;
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
            setDueDateWarning();
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
        setDueDateWarning();
    }

    public void toggleExpanded()
    {
        this.task.setExpanded(!this.task.getExpanded());
    }

    public void setDueDate()
    {
        // prevent error from user inputting value other than date
        try
        {
            this.task.setDueDate(this.dueDatePicker.getValue());
            setDueDateWarning();
        }
        catch (Exception e)
        {
        }
    }

    // warn user when due date is approaching for an incomplete task
    public void setDueDateWarning()
    {
        // get current time as long
        LocalDateTime dueDate = this.task.getDueDate().atStartOfDay();
        ZonedDateTime dueZonedDateTime = ZonedDateTime.of(dueDate, ZoneId.systemDefault());
        long currentTime = dueZonedDateTime.toInstant().toEpochMilli();

        // get due date as long
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime currentZonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        long dueTime = currentZonedDateTime.toInstant().toEpochMilli();

        // toggle notification when due date soon/passed and complete checkbox is not ticked
        if (currentTime > dueTime && !this.task.getComplete())
        {
            this.taskWarningLabel.setText("Task overdue");
            this.dueDateWarningImageView.setVisible(true);
        }
        else if (currentTime - 1000 * 60 * 60 * 24 > dueTime - 1000 * 60 * 60 * 24 * 7 && !this.task.getComplete())
        {
            this.taskWarningLabel.setText("Task due in " + ((dueTime - currentTime) / (1000 * 60 * 60 * 24)) + " days");
            this.dueDateWarningImageView.setVisible(true);
        }
        else
        {
            this.taskWarningLabel.setText("");
            this.dueDateWarningImageView.setVisible(false);
        }
    }
}
