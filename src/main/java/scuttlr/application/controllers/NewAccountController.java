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

import static scuttlr.application.Main.userController;

public class NewAccountController
{
    private byte[] avatarData;
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

    public void createAccount(ActionEvent actionEvent) throws IOException
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
            userController.getCurrentUser().setAvatar(this.avatarData);
            userController.saveUser();
            this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            userController.login(this.stage);
        }
    }

    public void uploadImage() throws IOException
    {
        // TODO filter file types
        // save avatar as byte array
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        BufferedImage image = ImageIO.read(new File(file.getPath()));
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outStreamObj);
        this.avatarData = outStreamObj.toByteArray();
    }
}