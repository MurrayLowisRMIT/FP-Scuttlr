package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scuttlr.application.model.User;

import java.io.File;
import java.io.IOException;

import static scuttlr.application.Main.boardController;
import static scuttlr.application.Main.userController;

public class UserController
{
    private Stage stage;
    private Scene scene;
    private AnchorPane pane;
    private User currentUser;

    public void createNewBoard(ActionEvent actionEvent)
    {
    }

    public void createUser(String username, String password)
    {
        this.currentUser = new User(username, password);
    }

    public void login(Stage stage) throws IOException
    {
        this.stage = stage;
        System.out.println(getClass().getResource("/scuttlr/application/display/board.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/board.fxml"));
        this.pane = loader.load();
        this.stage.setTitle(userController.getCurrentUser().getUsername());
        this.scene = new Scene(this.pane);
        this.stage.setScene(this.scene);
        this.stage.show();
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
        this.scene = new Scene(this.pane);
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public void loadUser(String username) throws IOException, ClassNotFoundException
    {
        Reader reader = new Reader();
        this.currentUser = reader.loadUser("src/main/resources/scuttlr/application/accounts/" + username + "_data.ser");

        try
        {
            boardController.loadBoards(username);
        }
        catch (ClassNotFoundException e)
        {
            // TODO failure to read boards
            throw new RuntimeException(e);
        }
    }

    public void saveUser()
    {
        Writer writer = new Writer();
        writer.saveUser();
    }

    public boolean checkUsernameAvailable(String username)
    {
        boolean usernameAvailable = false;
        File save = new File("src/main/resources/scuttlr/application/accounts/" + username + "_data.ser");
        if (!save.exists())
        {
            usernameAvailable = true;
        }
        return usernameAvailable;
    }
}