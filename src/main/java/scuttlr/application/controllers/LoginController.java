package scuttlr.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

import static scuttlr.application.Main.userController;

public class LoginController
{
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button verifyLoginButton;
    @FXML
    private Label usernameFailLabel;
    @FXML
    private Label passwordFailLabel;

    public void verifyLogin(ActionEvent actionEvent) throws RuntimeException
    {
        this.usernameFailLabel.setVisible(false);
        this.passwordFailLabel.setVisible(false);
        // TODO make this less shitty
        try
        {
            try
            {
                userController.loadUser(this.username.getText());
                if (userController.getCurrentUser().checkPassword(this.password.getText()))
                {
                    this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    userController.login(this.stage);
                }
                else
                {
                    this.passwordFailLabel.setVisible(true);
                }
            }
            catch (FileNotFoundException e)
            {
                this.usernameFailLabel.setVisible(true);
                throw new RuntimeException(e);
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
        catch (RuntimeException e)
        {
        }
    }

    public void goToNewAccount() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/newAccount.fxml"));
        this.pane = loader.load();
        this.stage = (Stage) this.verifyLoginButton.getScene().getWindow();
        this.scene = new Scene(this.pane);
        this.stage.setScene(this.scene);
    }
}