package scuttlr.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scuttlr.application.controllers.BoardController;
import scuttlr.application.controllers.UserController;

import java.io.IOException;

public class Main extends Application
{
    // board and user controllers are universal, so are publicly available
    public static UserController userController;
    public static BoardController boardController;

    @Override
    public void init()
    {
        userController = new UserController();
        boardController = new BoardController();
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/scuttlr/application/display/login.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image("/scuttlr/application/graphics/Logo.png");
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setTitle("Scuttlr");
        stage.setScene(scene);
        stage.show();
        // TODO set active board to users active board
    }

    @Override
    public void stop() throws Exception
    {
        // TODO terminate program
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}