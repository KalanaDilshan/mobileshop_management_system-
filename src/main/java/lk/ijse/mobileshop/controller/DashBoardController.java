package lk.ijse.mobileshop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class DashBoardController {

    public AnchorPane AllContext;

    public void btnHome(ActionEvent actionEvent) throws IOException {
        home();
    }

    public void initialize() throws IOException {
        home();
    }


    public void home() throws  IOException {
        URL resource = getClass().getResource("/view/DisplayDashBoard.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);

    }


    public void btnCustomer(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/customer_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);


    }

    public void btnPayment(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/Payment_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);

    }

    public void btnItem(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/item_stock_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);
    }
    public void btnEmployee(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/employee_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);
    }
    public void btnOrder(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/order_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);
    }
    public void btnSupplier(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/supplier_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);
    }
    public void btnRepair(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/repair_item_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);
    }
    public void btnLogOut(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/Login_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);
    }
    public void btnReload(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/reload_form.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        AllContext.getChildren().clear();
        AllContext.getChildren().add(load);
    }

}
