package scuttlr.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scuttlr.application.controllers.UserController;

public class Main extends Application
{
    // user controller is universal so is publicly statically available
    public static UserController userController;

    @Override
    public void init()
    {
        userController = new UserController();
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
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}