package scuttlr.application.controllers;

import javafx.animation.PauseTransition;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import scuttlr.application.model.Board;
import scuttlr.application.model.Column;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static scuttlr.application.Main.boardController;
import static scuttlr.application.Main.userController;

public class BoardController implements Initializable
{
    private Board activeBoard;
    private LinkedList<Board> loadedBoards;
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
    private Menu projectMenu;
    @FXML
    private MenuItem saveBoardMenuItem;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label quoteLabel;
    @FXML
    private Label notificationsLabel;
    @FXML
    private CheckBox completeCheckBox;
    @FXML
    protected ObservableList<Column> columns;
    @FXML
    protected ListView<Column> columnListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // load user's boards
        boardController.loadBoards(userController.getCurrentUser().getUsername());

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

    public void openBoard(ActionEvent actionEvent) throws IOException, ClassNotFoundException
    {
        ObservableList<String> loadedBoards = FXCollections.observableArrayList();
        loadedBoards.addAll(userController.getCurrentUser().getUserBoardNames());
        ListView<String> loadedBoardsListView = new ListView<>(loadedBoards);
        Stage loadMenu = new Stage();
        loadMenu.setScene(new Scene(loadedBoardsListView));
        loadMenu.show();
    }

    // create new temporary board with no password lock
    public void createNewBoard(ActionEvent actionEvent) throws IOException
    {
        String name = "New project";
        this.activeBoard = new Board(name, null);
        if (this.loadedBoards == null)
        {
            this.loadedBoards = new LinkedList<Board>();
        }
        this.loadedBoards.add(this.activeBoard);
        updateActiveBoardUI();
    }

    public Board getCurrentBoard()
    {
        return this.activeBoard;
    }

    public LinkedList<Board> getLoadedBoards()
    {
        return this.loadedBoards;
    }

    public void setCurrentBoard(Board board)
    {
        this.activeBoard = board;
        updateActiveBoardUI();
    }

    public void closeCurrentBoard()
    {
        this.activeBoard = null;
        this.columnListView.getItems().clear();
        updateActiveBoardUI();
    }

    // update UI elements based on whether a board is open
    public void updateActiveBoardUI()
    {
        this.stage = (Stage) this.menuBar.getScene().getWindow();
        if (this.activeBoard != null)
        {
            this.projectMenu.setDisable(false);
            this.saveBoardMenuItem.setDisable(false);
            this.stage.setTitle(userController.getCurrentUser().getUsername() + " - " + this.activeBoard.getBoardName());
        }
        else
        {
            this.projectMenu.setDisable(true);
            this.saveBoardMenuItem.setDisable(true);
            this.stage.setTitle(userController.getCurrentUser().getUsername() + " - No project selected");
        }
    }

    public void setBoardName(ActionEvent actionEvent)
    {
        // TODO
        this.stage = (Stage) this.menuBar.getScene().getWindow();
    }

    public void loadBoards(String username)
    {
        this.loadedBoards = new LinkedList<Board>();
        Reader reader = new Reader();
        // read user's boards from database
        LinkedList<String> tempBoardNames = userController.getCurrentUser().getUserBoardNames();
        for (int i = 0; i < userController.getCurrentUser().getUserBoardNames().size(); i++)
        {
            try
            {
                // open boards associated with the user - checks both the file name and the board's 'username' attribute
                this.loadedBoards.add(reader.loadBoard("src/main/resources/scuttlr/application/boards/" + username + "_" + tempBoardNames.get(i) + ".ser"));
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
        // set most recent board to active
        if (this.loadedBoards.size() > 0)
        {
            this.activeBoard = this.loadedBoards.getLast();
        }
    }

    public void saveBoard()
    {
        Writer writer = new Writer();
        writer.saveBoard(this.activeBoard);
    }

    // delete board save file
    public void deleteBoard()
    {
        String fileName = userController.getCurrentUser().getUsername() + "_" + this.activeBoard.getBoardName() + ".ser";
        File file = new File("src/main/resources/scuttlr/application/boards/" + fileName);
        if (file.delete())
        {
            setNotification(this.activeBoard.getBoardName() + " deleted");
        }
        else
        {
            setWarning(this.activeBoard.getBoardName() + " closed, no save file to delete");
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
        // TODO set this correctly
        this.columns.remove(actionEvent.getSource());
    }

    public void quit()
    {
        this.stage = (Stage) this.menuBar.getScene().getWindow();
        this.stage.close();
    }

    // red warning text
    public void setWarning(String notification)
    {
        this.notificationsLabel.setTextFill(Color.color(1, 0, 0));
        this.notificationsLabel.setText(notification);
        this.notificationsLabel.setVisible(true);
        // fade after 3 seconds
        PauseTransition notificationFade = new PauseTransition(Duration.seconds(3));
        notificationFade.setOnFinished(event -> this.notificationsLabel.setVisible(false));
        notificationFade.play();
    }

    // black notification text
    public void setNotification(String notification)
    {
        this.notificationsLabel.setTextFill(Color.color(0, 0, 0));
        this.notificationsLabel.setText(notification);
        this.notificationsLabel.setVisible(true);
        // fade after 3 seconds
        PauseTransition notificationFade = new PauseTransition(Duration.seconds(3));
        notificationFade.setOnFinished(event -> this.notificationsLabel.setVisible(false));
        notificationFade.play();
    }

    // TODO migrate below methods to other classes
    public void newTask(ActionEvent actionEvent)
    {
    }

    public void deleteTask(ActionEvent actionEvent)
    {
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