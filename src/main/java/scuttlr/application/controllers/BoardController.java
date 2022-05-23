package scuttlr.application.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scuttlr.application.model.Board;
import scuttlr.application.model.Column;

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
    private MenuBar menuBar;
    @FXML
    private MenuItem saveBoardMenuItem;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label quoteLabel;
    @FXML
    private CheckBox completeCheckBox;
    @FXML
    protected static ObservableList<Column> columns;
    @FXML
    protected static ListView<Column> columnListView;

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

        // populate columns
        columns = FXCollections.observableArrayList();
        columns.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                System.out.println("THING HAPPENED");
            }
        });
        columnListView = new ListView<Column>() ;
        columns.add(new Column("New column 1"));
        columns.add(new Column("New column 2"));
        columns.add(new Column("New column 3"));
        columnListView.setItems(columns);
    }

    public void openBoard(ActionEvent actionEvent) throws IOException
    {
        // TODO read board from database
    }

    public void createNewBoard(ActionEvent actionEvent) throws IOException
    {
        String name = "New board";
        this.activeBoard = userController.getCurrentUser().createBoard(name);
        this.saveBoardMenuItem.setDisable(false);
        if (this.userBoards == null)
        {
            this.userBoards = new LinkedList<Board>();
        }
        this.userBoards.add(this.activeBoard);
    }

    public void logout() throws IOException
    {
        userController.logout();
    }

    private Board activeBoard;
    private LinkedList<Board> userBoards;

    public Board getCurrentBoard()
    {
        return this.activeBoard;
    }

    public LinkedList<Board> getUserBoards()
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
        LinkedList<Board> tempUserBoards;
        // read user's boards from database
        if (this.userBoards != null)
        {
            tempUserBoards = new LinkedList<Board>();
            LinkedList<String> tempBoardNames = userController.getCurrentUser().getUserBoardNames();
            for (int i = 0; i < userController.getCurrentUser().getUserBoardNames().size(); i++)
            {
                tempUserBoards.add(reader.loadBoard("src/main/resources/scuttlr/application/boards/" + username + "_" + tempBoardNames.get(i) + "_data.ser"));
            }
        }
        else
        {
            // if user has no saved boards in database, create new temporary userBoards LinkedList
            this.userBoards = new LinkedList<Board>();
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