package scuttlr.application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import scuttlr.application.model.TaskList;
import scuttlr.application.model.Task;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;

public class TaskListController implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    private static class ListView
    {
        private String name;
        private int price;

        public String getName()
        {
            return name;
        }

        public int getPrice()
        {
            return price;
        }

        public ListView(String name, int price)
        {
            super();
            this.name = name;
            this.price = price;
        }
    }

    @FXML
    private LinkedHashSet<Task> tasks;

    public void defineList(TaskList list)
    {
    }

    public void createNewTask()
    {

    }
}
