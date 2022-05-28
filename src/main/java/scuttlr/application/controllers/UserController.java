package scuttlr.application.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scuttlr.application.model.User;

import java.io.File;
import java.io.IOException;

import static scuttlr.application.Main.userController;
import static scuttlr.application.Main.boardController;

public class UserController
{
    private Stage stage;
    private Scene scene;
    private AnchorPane pane;
    private User currentUser;

    public void createUser(String username, String password)
    {
        this.currentUser = new User(username, password);
    }

    public void login(Stage stage) throws IOException
    {
        this.stage = new Stage();
        Image icon = new Image("/scuttlr/application/graphics/Logo.png");
        stage.getIcons().add(icon);
        this.stage = stage;
        this.stage.setResizable(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/board.fxml"));
        this.pane = new AnchorPane();
        try
        {
            this.pane = loader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        this.stage.setTitle(userController.getCurrentUser().getUsername() + " - No project selected");
        this.scene = new Scene(this.pane);
        this.stage.setScene(this.scene);
        this.stage.show();
        // open user's most recent board on log in
        boardController = loader.getController();
        boardController.openSelectedBoard(userController.currentUser.getCurrentBoardName());
    }

    public User getCurrentUser()
    {
        return this.currentUser;
    }

    public void logout() throws IOException
    {
        this.currentUser = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/login.fxml"));
        this.pane = loader.load();
        this.stage.close();
        this.stage = new Stage();
        this.stage.setTitle("Scuttlr");
        this.scene = new Scene(this.pane);
        this.stage.setScene(this.scene);
        Image icon = new Image("/scuttlr/application/graphics/Logo.png");
        stage.getIcons().add(icon);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void loadUser(String username) throws IOException, ClassNotFoundException
    {
        Reader reader = new Reader();
        this.currentUser = reader.loadUser("src/main/resources/scuttlr/application/accounts/" + username + ".ser");
    }

    public void saveUser()
    {
        Writer writer = new Writer();
        writer.saveUser();
    }

    public boolean checkUsernameAvailable(String username)
    {
        boolean usernameAvailable = false;
        File save = new File("src/main/resources/scuttlr/application/accounts/" + username + ".ser");
        if (!save.exists())
        {
            usernameAvailable = true;
        }
        return usernameAvailable;
    }
}