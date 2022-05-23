package scuttlr.application.controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import static scuttlr.application.Main.boardController;
import static scuttlr.application.Main.userController;

public class NewAccountController implements Initializable
{
    private boolean customAvatar = false;
    private byte[] avatar;
    @FXML
    private ImageView logoImageView;
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
    private Label avatarFailLabel;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private AnchorPane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // rotating logo
        RotateTransition rotation = new RotateTransition();
        rotation.setNode(this.logoImageView);
        rotation.setDuration(Duration.millis(5000));
        rotation.setCycleCount(TranslateTransition.INDEFINITE);
        rotation.setInterpolator(Interpolator.LINEAR);
        rotation.setByAngle(360);
        rotation.setAxis(Rotate.Z_AXIS);
        rotation.play();
    }

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
        this.avatarFailLabel.setVisible(false);

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
            if (this.customAvatar)
            {
                userController.getCurrentUser().setAvatar(this.avatar);
            }
            else
            {
                userController.getCurrentUser().setAvatar(defaultAvatar());
            }
            userController.saveUser();
            boardController.loadBoards(username);
            this.stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            userController.login(this.stage);
        }
    }

    public void uploadImage() throws IOException
    {
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
            this.avatar = outStreamObj.toByteArray();
            this.avatarFailLabel.setVisible(false);

            // do not use default avatar
            if (this.avatar != null)
            {
                this.customAvatar = true;
                this.avatarConfirmedCheckbox.setSelected(true);
            }
        }
        else
        {
            this.avatarFailLabel.setVisible(true);
        }
    }

    // set default avatar if none selected
    public byte[] defaultAvatar() throws IOException
    {
        // save avatar as byte array
        byte[] avatarData;
        BufferedImage image = ImageIO.read(new File("src/main/resources/scuttlr/application/graphics/Generic_Avatar.png"));
        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outStreamObj);
        avatarData = outStreamObj.toByteArray();
        return avatarData;
    }
}