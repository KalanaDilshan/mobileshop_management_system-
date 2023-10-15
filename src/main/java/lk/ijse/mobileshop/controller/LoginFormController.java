package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginFormController {

    public AnchorPane LoginFormContext;
    public JFXTextField txtUser;
    public JFXTextField txtPassword;
    public Label lblLog;

    public void btnLogin(ActionEvent actionEvent) throws IOException {

        String user = "k";
        String password = "2002";
        if (txtUser.getText().equals(user) && txtPassword.getText().equals(password)) {
            Stage window = (Stage) LoginFormContext.getScene().getWindow();
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml "))));
            window.centerOnScreen();

        } else if (txtUser.getText().isEmpty() && txtPassword.getText().isEmpty()) {
            lblLog.setText("Your User Name Or Password IS Empty...!");
            txtUser.clear();
            txtPassword.clear();
        }
        else if (!txtUser.getText().equals(user)) {
           lblLog.setText("Your User Name is incorrect..!");
            txtUser.clear();
            txtPassword.clear();
        } else if (!txtPassword.getText().equals(password)) {
           lblLog.setText("Your Password is incorrect..!");
            txtUser.clear();
            txtPassword.clear();
        }
    }
}
