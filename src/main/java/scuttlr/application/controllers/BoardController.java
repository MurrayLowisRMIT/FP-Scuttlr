package scuttlr.application.controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import scuttlr.application.model.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static scuttlr.application.Main.boardController;
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
    private MenuBar menuBar;
    @FXML
    private MenuItem newBoardMenuItem;
    @FXML
    private MenuItem saveBoardMenuItem;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label quoteLabel;
    @FXML
    private CheckBox completeCheckBox;
    @FXML
    private ObservableList<TaskListController> taskLists;
    @FXML
    private ListView<TaskListController> listView;

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
        this.listView = new ListView<TaskListController>(taskLists);
        this.listView.setLayoutX(200);
        this.listView.setLayoutY(200);
        this.listView.setCellFactory(new Callback<ListView<TaskListController>, ListCell<TaskListController>>()
        {
            @Override
            public ListCell<TaskListController> call(ListView<TaskListController> listView)
            {
                return new ListCell<TaskListController>();
            }
        });

        this.pane.getChildren().add(listView);

        // TODO populate actual boards into boardController
        LinkedHashSet<Board> boards = boardController.getUserBoards();
        Iterator<Board> i = boards.iterator();
        while (i.hasNext())
        {
            taskLists.add(new TaskListController(i.next()));
        }
    }

    public void openBoard(ActionEvent actionEvent) throws IOException
    {
        // TODO read board from database
    }

    public void createNewBoard(ActionEvent actionEvent) throws IOException
    {
        this.activeBoard = userController.getCurrentUser().createBoard("New project");
        this.saveBoardMenuItem.setDisable(false);
        if (this.userBoards == null)
        {
            this.userBoards = new LinkedHashSet<Board>();
        }
        this.userBoards.add(this.activeBoard);
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
        // read user's boards from database
        LinkedHashSet<Board> userBoards = this.userBoards;
        if (userBoards != null)
        {
            for (Board board : userBoards)
            {
                this.userBoards.add(reader.loadBoard("src/main/resources/scuttlr/application/boards/" + username + "_" + board.getBoardName() + "_data.ser"));
            }
        }
        else
        {
            // if user has no saved boards in database, create new temporary userBoards LinkedHashSet
            this.userBoards = new LinkedHashSet<Board>();
        }
    }

    public void saveBoard()
    {
        if (this.activeBoard != null)
        {
            Writer writer = new Writer();
            writer.saveBoard(this.activeBoard);
        }
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

    public void quit(ActionEvent actionEvent)
    {
        this.stage = (Stage) menuBar.getScene().getWindow();
        this.stage.close();
    }

    // toggle completed status for task
    public void checkComplete(ActionEvent actionEvent)
    {
        this.completeCheckBox.setSelected(!this.completeCheckBox.isSelected());
        if (this.completeCheckBox.isSelected())
        {

        }
        else
        {

        }
    }
}