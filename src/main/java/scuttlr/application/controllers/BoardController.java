package scuttlr.application.controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    private static Board activeBoard;
    private LinkedList<Board> loadedBoards;
    @FXML
    private Stage stage;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private ImageView newColumnButtonImageView;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu projectMenu;
    @FXML
    private MenuItem saveBoardMenuItem;
    @FXML
    private Label projectNameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label notificationsLabel;
    @FXML
    private ObservableList<Column> columns;
    @FXML
    private ListView columnsListView;
    @FXML
    private LinkedList<Pane> columnPanes;
    private LinkedList<ColumnController> columnControllers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
//        this.columnsListView.height
        // read user's boards
        this.loadedBoards = new LinkedList<>();
        loadBoards(userController.getCurrentUser().getUsername());

        // set username to label
        this.usernameLabel.setText(userController.getCurrentUser().getUsername());

        // set project name to label
        this.projectNameLabel.setText("No project selected");

        // read daily quote
        Reader reader = new Reader();
        setNotification(reader.loadQuote());

        // read avatar
        setAvatarImageView();

        // read and populate columns
        this.columnPanes = new LinkedList<>();
        this.columns = FXCollections.observableArrayList();
    }

    // read avatar from byte array to FXImage
    public void setAvatarImageView()
    {
        ByteArrayInputStream in = new ByteArrayInputStream(userController.getCurrentUser().getAvatar());
        BufferedImage image;
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
    }

    // create popup menu to select user projects
    public void openBoard()
    {
        if (this.loadedBoards.size() > 0)
        {
            ObservableList<Object> loadedBoards = FXCollections.observableArrayList();
            ListView<Object> loadedBoardsListView = new ListView<>(loadedBoards);
            Label title = new Label("Select project");
            title.setPrefWidth(200);
            title.setAlignment(Pos.CENTER);
            loadedBoardsListView.getItems().add(title);
            // add a button to popup for each user owned project
            for (int i = 0; i < userController.getCurrentUser().getUserBoardNames().size(); i++)
            {
                int x = i;
                Button button = new Button(userController.getCurrentUser().getUserBoardNames().get(i));
                button.setOnAction(e -> openSelectedBoard(e));
                button.setPrefWidth(200);
                loadedBoardsListView.getItems().add(button);
            }
            Stage loadMenu = new Stage();
            Scene scene = new Scene(loadedBoardsListView);
            loadMenu.setWidth(240);
            loadMenu.setHeight(200);
            loadMenu.setResizable(false);
            Image icon = new Image("scuttlr/application/graphics/Logo.png");
            loadMenu.getIcons().add(icon);
            loadMenu.setScene(scene);
            loadMenu.show();
        }
        else
        {
            setWarning("No projects available");
        }
    }

    // open board selected from openBoard method and close popup menu on selection
    public void openSelectedBoard(ActionEvent event)
    {
        String boardName = ((Button) event.getSource()).getText();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        for (int i = 0; i < this.loadedBoards.size(); i++)
        {
            if (this.loadedBoards.get(i).getBoardName().matches(boardName))
            {
                this.activeBoard = this.loadedBoards.get(i);
            }
        }
        updateBoardController();
    }

    // create new temporary board with no password lock
    public void createNewBoard(ActionEvent actionEvent) throws IOException
    {
        String name = "New project";
        this.activeBoard = new Board(name, null);
        updateBoardController();
    }

    public void closeCurrentBoard()
    {
        this.activeBoard = null;
        this.columnsListView.getItems().clear();
        updateBoardController();
    }

    // TODO transfer columns to new file
    // open dialogue to change name of currently active board
    public void updateBoardName()
    {
        this.stage = (Stage) this.menuBar.getScene().getWindow();
        VBox vBox = new VBox();
        Stage loadMenu = new Stage();
        TextField textInput = new TextField();
        textInput.setPromptText("Enter new project name");
        Button confirm = new Button("Confirm");
        confirm.setPrefWidth(200);
        confirm.setAlignment(Pos.CENTER);
        confirm.setOnAction(e ->
        {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();
            if (textInput.getText().length() == 0)
            {
                this.setWarning("Project name cannot be blank");
            }
            else if (userController.getCurrentUser().updateBoardName(this.activeBoard, textInput.getText()))
            {
                this.setNotification("Project name updated");
                for (int i = 0; i < this.loadedBoards.size(); i++)
                {
                    if (userController.getCurrentUser().getUserBoardNames().get(i).matches(userController.getCurrentUser().getCurrentUserBoardName()))
                    {
                        this.activeBoard = this.loadedBoards.get(i);
                    }
                }
                // refresh loaded boards
                loadBoards(userController.getCurrentUser().getUsername());
                updateBoardController();
            }
            else
            {
                this.setWarning("Name in use by another project");
            }
        });
        vBox.getChildren().addAll(textInput, confirm);
        Scene scene = new Scene(vBox);
        loadMenu.setWidth(200);
        loadMenu.setHeight(89);
        loadMenu.setResizable(false);
        Image icon = new Image("scuttlr/application/graphics/Logo.png");
        loadMenu.getIcons().add(icon);
        loadMenu.setScene(scene);
        loadMenu.show();
    }

    // open dialogue to change username
    public void updateUsername()
    {
        VBox vBox = new VBox();
        Stage loadMenu = new Stage();
        TextField textInput = new TextField();
        textInput.setPromptText("Enter new username");
        Button confirm = new Button("Confirm");
        confirm.setPrefWidth(200);
        confirm.setAlignment(Pos.CENTER);
        // lambda to control popup function
        confirm.setOnAction(e ->
        {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();
            if (textInput.getText().length() == 0)
            {
                this.setWarning("Username cannot be blank");
            }
            else if (userController.getCurrentUser().updateUsername(textInput.getText(), this.loadedBoards))
            {
                // refresh board UI
                this.usernameLabel.setText(textInput.getText());
                this.setNotification("Username updated");
                // refresh loadedBoards list
                this.loadedBoards = new LinkedList<>();
                loadBoards(userController.getCurrentUser().getUsername());
                for (int i = 0; i < this.loadedBoards.size(); i++)
                {
                    if (userController.getCurrentUser().getUserBoardNames().get(i).matches(userController.getCurrentUser().getCurrentUserBoardName()))
                    {
                        this.activeBoard = this.loadedBoards.get(i);
                    }
                }
                // refresh loaded boards
                loadBoards(userController.getCurrentUser().getUsername());
                updateBoardController();
            }
            else
            {
                this.setWarning("Username taken");
            }
        });

        // design popup
        vBox.getChildren().addAll(textInput, confirm);
        Scene scene = new Scene(vBox);
        loadMenu.setWidth(200);
        loadMenu.setHeight(89);
        loadMenu.setResizable(false);
        Image icon = new Image("scuttlr/application/graphics/Logo.png");
        loadMenu.getIcons().add(icon);
        loadMenu.setScene(scene);
        loadMenu.show();
    }

    // update UI and memory elements after changes
    public void updateBoardController()
    {
        this.stage = (Stage) this.menuBar.getScene().getWindow();
        // toggle title enabled menu  elements
        if (this.activeBoard != null)
        {
            this.projectMenu.setDisable(false);
            this.saveBoardMenuItem.setDisable(false);
            this.newColumnButtonImageView.setVisible(true);
            this.stage.setTitle(userController.getCurrentUser().getUsername() + " - " + this.activeBoard.getBoardName());
            this.projectNameLabel.setText(this.activeBoard.getBoardName());
        }
        else
        {
            this.projectMenu.setDisable(true);
            this.saveBoardMenuItem.setDisable(true);
            this.newColumnButtonImageView.setVisible(false);
            this.stage.setTitle(userController.getCurrentUser().getUsername() + " - No project selected");
            this.projectNameLabel.setText("No project selected");
        }
        updateColumns();
    }

    public void newColumn()
    {
        this.activeBoard.addColumn();
        updateColumns();
    }

    // update display of columns within board
    public void updateColumns()
    {
        if (this.activeBoard != null)
        {
            // TODO dynamically populate instead of fully deleting and rebuilding every time
            this.columnsListView.getItems().clear();
            this.columnPanes.clear();
            this.columnControllers = new LinkedList<>();
            for (int i = 0; i < this.activeBoard.getColumns().size(); i++)
            {
                // create and store controller in columnControllers list
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/column.fxml"));
                try
                {
                    this.columnPanes.add(loader.load());
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                this.columnControllers.add(loader.getController());
                this.columnControllers.getLast().setColumn(this.activeBoard.getColumns().getLast());
            }
            this.columnsListView.getItems().addAll(this.columnPanes);
        }
    }

    // read user's boards from database to memory
    public void loadBoards(String username)
    {
        Reader reader = new Reader();
        LinkedList<String> tempBoardNames = userController.getCurrentUser().getUserBoardNames();
        this.loadedBoards = new LinkedList<>();
        for (int i = 0; i < userController.getCurrentUser().getUserBoardNames().size(); i++)
        {
            try
            {
                // open boards associated with the user - checks both the file name and the board's 'username' attribute
                this.loadedBoards.add(reader.loadBoard("src/main/resources/scuttlr/application/boards/" + username + "_" + tempBoardNames.get(i) + ".ser"));
            }
            catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
        // set most recent board as active
        if (this.loadedBoards.size() > 0)
        {
            for (int i = 0; i < this.loadedBoards.size(); i++)
            {
                if (this.loadedBoards.get(i).getBoardName().matches(userController.getCurrentUser().getCurrentUserBoardName()))
                {
                    this.activeBoard = loadedBoards.get(i);
                }
            }
        }
    }

    public void saveBoard()
    {
        Writer writer = new Writer();
        writer.saveBoard(this.activeBoard);
        // refresh loaded boards
        loadBoards(userController.getCurrentUser().getUsername());
        updateBoardController();
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
        // update user account with changes
        userController.getCurrentUser().removeBoardFromUser(this.activeBoard.getBoardName());
        closeCurrentBoard();
        // refresh loaded boards
        loadBoards(userController.getCurrentUser().getUsername());
        updateBoardController();
    }

    public void logout() throws IOException
    {
        userController.logout();
    }

    // add controller for new column and pass column for it to control
    //    public Column addColumnController(ColumnController columnController)
    //    {
    //        columnControllers.add(columnController);
    //        return this.active.getColumns().getLast();
    //    }

    // TODO delete column via columnID
    public void deleteColumn(int columnID)
    {
        // TODO set this correctly
        //        this.columns.remove(columnID);
        this.activeBoard.getColumns().remove(columnID);
        updateBoardController();
    }

    // TODO delete column via actionEvent
    public void deleteSelectedColumn(MouseEvent mouseEvent)
    {
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
        // fade after 5 seconds
        PauseTransition notificationFade = new PauseTransition(Duration.seconds(5));
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
        PauseTransition notificationFade = new PauseTransition(Duration.seconds(5));
        notificationFade.setOnFinished(event -> this.notificationsLabel.setVisible(false));
        notificationFade.play();
    }

    public void changeAvatar(MouseEvent mouseEvent)
    {
        VBox vBox = new VBox();
        Stage loadMenu = new Stage();
        Button newAvatar = new Button("Change avatar");
        Button removeAvatar = new Button("Default avatar");
        newAvatar.setPrefWidth(200);
        removeAvatar.setPrefWidth(200);
        newAvatar.setOnAction(e ->
        {
            try
            {
                selectNewAvatar(e);
            }
            catch (IOException ex)
            {
                setWarning("Read error");
                throw new RuntimeException(ex);
            }
        });
        removeAvatar.setOnAction(e ->
        {
            try
            {
                removeAvatar(e);
            }
            catch (IOException ex)
            {
                setWarning("Read error");
                throw new RuntimeException(ex);
            }
        });
        vBox.getChildren().addAll(newAvatar, removeAvatar);
        Scene scene = new Scene(vBox);
        loadMenu.setWidth(200);
        loadMenu.setHeight(89);
        loadMenu.setResizable(false);
        Image icon = new Image("scuttlr/application/graphics/Logo.png");
        loadMenu.getIcons().add(icon);
        loadMenu.setScene(scene);
        loadMenu.show();
    }

    // select new user avatar
    public void selectNewAvatar(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        // save avatar as byte array
        // TODO filter file types
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        // avatar only allowed to be png format
        if (file != null && file.getAbsoluteFile().toString().contains(".png"))
        {
            BufferedImage image = ImageIO.read(new File(file.getPath()));
            ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outStreamObj);
            userController.getCurrentUser().setAvatar(outStreamObj.toByteArray());
            setAvatarImageView();
            setNotification("Avatar updated");
        }
        else
        {
            setWarning(".png format only");
        }
    }

    // set default avatar
    public void removeAvatar(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        BufferedImage image = ImageIO.read(new File("src/main/resources/scuttlr/application/graphics/Logo.png"));
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outStreamObj);
        userController.getCurrentUser().setAvatar(outStreamObj.toByteArray());
        setAvatarImageView();
        setNotification("Avatar removed");
    }
}