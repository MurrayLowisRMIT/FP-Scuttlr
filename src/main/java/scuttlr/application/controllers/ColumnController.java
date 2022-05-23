package scuttlr.application.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import scuttlr.application.model.Column;
import scuttlr.application.model.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;

import static scuttlr.application.Main.boardController;
import static scuttlr.application.Main.userController;

public class ColumnController extends BoardController implements Initializable
{
    @FXML
    private Label titleLabel;
    @FXML
    private LinkedHashSet<Task> tasks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void createNewTask()
    {

    }

    public void deleteColumn(ActionEvent actionEvent)
    {
    }
}
