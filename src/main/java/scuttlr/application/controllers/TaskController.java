package scuttlr.application.controllers;

import scuttlr.application.model.Task;

import java.util.ArrayList;

public class TaskController
{
    private ArrayList<Task> tasks;

    public TaskController()
    {
        this.tasks = new ArrayList<Task>();
    }
}
