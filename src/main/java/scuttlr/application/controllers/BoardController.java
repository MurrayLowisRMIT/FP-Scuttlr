package scuttlr.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import scuttlr.application.model.Board;
import scuttlr.application.model.TaskList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static scuttlr.application.Main.userController;

public class BoardController implements Initializable
{
    @FXML
    private Stage stage;
    @FXML
    private AnchorPane pane;
    @FXML
    private Scene scene;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private MenuItem newBoardMenuItem;
    @FXML
    private MenuItem saveBoardMenuItem;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label quoteLabel;
    @FXML
    private ObservableList<TaskList> taskLists;
    @FXML
    private ListView<TaskList> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // set username to label
        this.usernameLabel.setText(userController.getCurrentUser().getUsername());

        // set quote
        Reader reader = new Reader();
        this.quoteLabel.setText(reader.loadQuote());

        // read avatar from byte array to FXImage
        ByteArrayInputStream in = new ByteArrayInputStream(userController.getCurrentUser().getAvatar());
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(in);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        WritableImage avatar;
        avatar = new WritableImage(image.getWidth(), image.getHeight());
        PixelWriter pw = avatar.getPixelWriter();
        for (int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                pw.setArgb(x, y, image.getRGB(x, y));
            }
        }
        this.avatarImageView.setImage(avatar);

        // populate taskLists
        this.taskLists = FXCollections.observableArrayList();
        this.listView = new ListView<TaskList>(taskLists);
        this.listView.setCellFactory(new Callback<ListView<TaskList>, ListCell<TaskList>>()
        {
            @Override
            public ListCell<TaskList> call(ListView<TaskList> listView)
            {
                return new ListCell<TaskList>();
            }
        });
    }

    public void openBoard(ActionEvent actionEvent) throws IOException
    {
        // TODO read board from database
    }

    public void createNewBoard(ActionEvent actionEvent) throws IOException
    {
        // TODO need to add function to update password
        this.activeBoard = userController.getCurrentUser().createBoard("New project");
        this.saveBoardMenuItem.setDisable(false);
    }

    public void createNewList(ActionEvent actionEvent) throws IOException
    {
    }

    public void logout() throws IOException
    {
        userController.logout();
    }

    private Board activeBoard;
    private LinkedHashSet<Board> userBoards;

    public Board getCurrentBoard()
    {
        return this.activeBoard;
    }

    public LinkedHashSet<Board> getUserBoards()
    {
        return this.userBoards;
    }

    public void setCurrentBoard(Board board)
    {
        this.activeBoard = board;
    }

    public void closeCurrentBoard()
    {
        this.activeBoard = null;
    }

    public void setBoardName(String boardName)
    {
        this.activeBoard.setBoardName(boardName);
    }

    public void loadBoards(String username) throws IOException, ClassNotFoundException
    {
        Reader reader = new Reader();
        for (String boardName : userController.getCurrentUser().getUserBoards())
        {
            System.out.println(boardName);
            this.userBoards.add(reader.loadBoard("src/main/resources/scuttlr/application/boards/" + username + "_" + boardName + "_data.ser"));
        }
    }

    public void saveBoard()
    {
        Writer writer = new Writer();
        writer.saveBoard(this.activeBoard);
        // userHandler.getCurrentUser().saveBoard(this.activeBoard.getBoardName());
    }

    public void deleteList(ActionEvent actionEvent)
    {
    }

    public void createNewTask(ActionEvent actionEvent)
    {
    }

    public void deleteTask(ActionEvent actionEvent)
    {
    }
}