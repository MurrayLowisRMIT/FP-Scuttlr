package scuttlr.application.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scuttlr.application.model.Board;
import scuttlr.application.model.Column;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static scuttlr.application.Main.userController;

public class BoardController implements Initializable
{
    private Board activeBoard;
    private LinkedList<Board> userBoards;
    @FXML
    private Stage stage;
    @FXML
    private Pane columnsPane;
    @FXML
    private Scene scene;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem saveBoardMenuItem;
    @FXML
    private HBox boardControlsHBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label quoteLabel;
    @FXML
    private CheckBox completeCheckBox;
    @FXML
    protected ObservableList<Column> columns;
    @FXML
    protected ListView<Column> columnListView;

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
        this.columns = FXCollections.observableArrayList();
        this.columnListView = new ListView<>();
        this.columnListView.setItems(this.columns);
        this.columnListView.setOrientation(Orientation.HORIZONTAL);
        this.columnListView.prefWidthProperty().bind(this.columnsPane.widthProperty().subtract(10));
        this.columnListView.prefHeightProperty().bind(this.columnsPane.heightProperty().subtract(10));
        this.columnListView.setLayoutX(5);
        this.columnListView.setLayoutY(5);

        columnListView.setCellFactory(param -> new ColumnController()
        {
        });

        //        columnListView.setCellFactory(param -> new ListCell<Column>()
        //        {
        //            @Override
        //            protected void updateItem(Column column, boolean empty)
        //            {
        //                super.updateItem(column, empty);
        //                if (empty || column == null)
        //                {
        //                    setText(null);
        //                }
        //                else
        //                {
        //                    setText(column.getTitle());
        //                }
        //            }
        //        });

        this.columnsPane.getChildren().add(this.columnListView);

        this.columns.addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                System.out.println("THING HAPPENED");
            }
        });
    }

    public void openBoard(ActionEvent actionEvent) throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null && file.getAbsoluteFile().toString().matches(userController.getCurrentUser().getUsername() + "_" + this.activeBoard.getBoardName() + ".ser"))
        {
            // TODO activate file
        }
        else
        {
            // TODO set popup error warning
        }
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
        toggleBoardActiveUI();
    }

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
        toggleBoardActiveUI();
    }

    public void closeCurrentBoard()
    {
        this.activeBoard = null;
        this.columnListView.getItems().clear();
        toggleBoardActiveUI();
    }

    // toggle UI elements based on whether a board is open
    public void toggleBoardActiveUI()
    {
        this.stage = (Stage) this.menuBar.getScene().getWindow();
        if (this.activeBoard != null)
        {
            this.boardControlsHBox.setDisable(false);
            this.stage.setTitle(userController.getCurrentUser().getUsername() + " - " + this.activeBoard.getBoardName());
        }
        else
        {
            this.boardControlsHBox.setDisable(true);
            this.stage.setTitle(userController.getCurrentUser().getUsername() + " - No project selected");
        }
    }

    public void setBoardName(String boardName)
    {
        this.activeBoard.setBoardName(boardName);
    }

    public void loadBoards(String username)
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
                try
                {
                    // open boards associated with the user - checks both the file name and the board's 'username' attribute
                    tempUserBoards.add(reader.loadBoard("src/main/resources/scuttlr/application/boards/" + username + "_" + tempBoardNames.get(i) + "_data.ser"));
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                catch (ClassNotFoundException e)
                {
                    throw new RuntimeException(e);
                }
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

    // delete board save file
    public void deleteBoard()
    {
        String fileName = userController.getCurrentUser().getUsername() + "_" + this.activeBoard.getBoardName() + ".ser";
        File file = new File("src/main/resources/scuttlr/application/boards/" + fileName);
        if (file.delete())
        {
            // TODO notify user of success
        }
        else
        {
            // TODO notify user of failure
        }
        closeCurrentBoard();
    }

    public void logout() throws IOException
    {
        userController.logout();
    }

    public void newColumn()
    {
        this.columns.add(new Column("New column"));
    }

    public void deleteColumn(ActionEvent actionEvent)
    {
        this.columns.remove(actionEvent.getSource());
    }

    public void newTask(ActionEvent actionEvent)
    {
    }

    public void deleteTask(ActionEvent actionEvent)
    {
    }

    public void quit(ActionEvent actionEvent)
    {
        this.stage = (Stage) this.menuBar.getScene().getWindow();
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