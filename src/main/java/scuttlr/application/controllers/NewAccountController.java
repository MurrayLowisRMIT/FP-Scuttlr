package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static scuttlr.application.Main.boardController;
import static scuttlr.application.Main.userController;

public class NewAccountController
{
    private boolean customAvatar = false;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private CheckBox avatarConfirmedCheckbox;
    @FXML
    private Label usernameFailLabel;
    @FXML
    private Label passwordFailLabel;
    @FXML
    private Label confirmPasswordFailLabel;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private AnchorPane pane;

    public void goToLogin(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/login.fxml"));
        this.pane = loader.load();
        this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        this.scene = new Scene(this.pane);
        this.stage.setScene(this.scene);
    }

    public void createAccount(ActionEvent actionEvent) throws IOException, ClassNotFoundException
    {
        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.confirmPassword.getText();

        boolean failure = false;
        this.usernameFailLabel.setVisible(false);
        this.passwordFailLabel.setVisible(false);
        this.confirmPasswordFailLabel.setVisible(false);

        if (!userController.checkUsernameAvailable(username))
        {
            this.usernameFailLabel.setVisible(true);
            this.usernameFailLabel.setText("Username taken");
            failure = true;
        }

        if (username.length() == 0)
        {
            this.usernameFailLabel.setVisible(true);
            this.usernameFailLabel.setText("Username can't be blank");
            failure = true;
        }

        // TODO password regex
        if (password.length() < 8)
        {
            this.passwordFailLabel.setVisible(true);
            failure = true;
        }

        if (!password.matches(confirmPassword))
        {
            failure = true;
            this.confirmPasswordFailLabel.setVisible(true);
        }

        if (!failure)
        {
            userController.createUser(username, password);
            userController.getCurrentUser().setAvatar(createAvatar());
            userController.saveUser();
            boardController.loadBoards(username);
            this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            userController.login(this.stage);
        }
    }

    public void uploadImage()
    {
        this.customAvatar = true;
    }

    public byte[] createAvatar() throws IOException
    {
        // save avatar as byte array
        byte[] avatarData;
        BufferedImage image;
        if (this.customAvatar)
        {
            // TODO filter file types
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            image = ImageIO.read(new File(file.getPath()));
        }
        else
        {
            image = ImageIO.read(new File("src/main/resources/scuttlr/application/graphics/Generic_Avatar.png"));
        }
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outStreamObj);
        avatarData = outStreamObj.toByteArray();
        return avatarData;
    }
}