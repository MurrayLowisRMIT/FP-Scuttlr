package scuttlr.application.controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static scuttlr.application.Main.userController;

public class LoginController implements Initializable
{
    @FXML
    private ImageView logoImageView;
    @FXML
    private RotateTransition logoRotation;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // rotating logo
        this.logoRotation = new RotateTransition();
        this.logoRotation.setNode(this.logoImageView);
        this.logoRotation.setDuration(Duration.millis(5000));
        this.logoRotation.setCycleCount(RotateTransition.INDEFINITE);
        this.logoRotation.setInterpolator(Interpolator.LINEAR);
        this.logoRotation.setByAngle(360);
        this.logoRotation.setAxis(Rotate.Z_AXIS);
        this.logoRotation.play();
    }

    public void verifyLogin(ActionEvent actionEvent) throws RuntimeException
    {
        // hide error messages until triggered
        this.usernameFailLabel.setVisible(false);
        this.passwordFailLabel.setVisible(false);
        // TODO make this less shitty
        try
        {
            try
            {
                // check if a user file exists for the username entered and load if so
                userController.loadUser(this.username.getText());
                // if password matches, log in
                if (userController.getCurrentUser().checkPassword(this.password.getText()))
                {
                    // open main screen in new window
                    this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    this.stage.close();
                    this.stage = new Stage();
                    Image icon = new Image("/scuttlr/application/graphics/Logo.png");
                    stage.getIcons().add(icon);
                    this.stage.setResizable(true);
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
        // go to new account screen in same window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scuttlr/application/display/newAccount.fxml"));
        this.pane = loader.load();
        this.stage = (Stage) this.verifyLoginButton.getScene().getWindow();
        this.scene = new Scene(this.pane);
        this.stage.setScene(this.scene);
    }
}