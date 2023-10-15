package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Cart;
import lk.ijse.mobileshop.dto.ItemStock;
import lk.ijse.mobileshop.model.CustomerModel;
import lk.ijse.mobileshop.model.OrderModel;
import lk.ijse.mobileshop.tm.OrderTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderFormController  implements Initializable {
   
    public TableView tblOrderDetails;
    public AnchorPane AllContext;
    public JFXTextField txtQty;
    public JFXTextField txtItemName;
    public TableColumn colItemId;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colAction;
    public JFXComboBox cmbCustomeId;
    public JFXComboBox cmbItemId;
    public Label lblOrdrId;
    public Label lblTotal;
    public JFXTextField txtnitPrice;
    public Label displayDate;
    public AnchorPane anchorpayn1;

    ObservableList<OrderTm> obList = FXCollections.observableArrayList();

    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

    public void txtKeyPress(KeyEvent keyEvent) {
    }
    private void DisplayDate(){
        Timeline time=new Timeline(
                new KeyFrame(Duration.ZERO, e ->{
                    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd    HH:mm:ss");
                    displayDate.setText(formatter.format(LocalDateTime.now()));

                }),new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    private void generateNextOrderId() {
        try {
            String nextId = OrderModel.generateNextId();
            lblOrdrId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void setCmb() throws SQLException {
        List<String> customerId = CustomerModel.getId();
        cmbCustomeId.getItems().addAll(customerId);
        List<String> itemId = ItemStock.getId();
        cmbItemId.getItems().addAll(itemId);
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
       String customerId = String.valueOf(cmbCustomeId.getValue());
       String orderId = lblOrdrId.getText();
       String itemId = String.valueOf(cmbItemId.getValue());
       String itemName = txtItemName.getText();
       int qty = Integer.parseInt(txtQty.getText());
       double unitPrice = Double.parseDouble(txtnitPrice.getText());

        double total = qty * unitPrice;
        Button btnRemove = new Button("Remove");

        btnRemove.setCursor(Cursor.HAND);

        setRemoveBtnOnAction(btnRemove);

        if (!obList.isEmpty()) {
            for (int i = 0; i < tblOrderDetails.getItems().size(); i++) {
                if (colItemId.getCellData(i).equals(itemId)) {
                    qty += (int) colQty.getCellData(i);
                    total = qty * unitPrice;

                    obList.get(i).setQty(qty);
                    obList.get(i).setTotal(total);

                    tblOrderDetails.refresh();
                    calculateNetTotal();
                    return;
                }
            }
        }
        OrderTm tm = new OrderTm(itemId, qty,total,btnRemove);

        obList.add(tm);
        tblOrderDetails.setItems(obList);
        calculateNetTotal();
    }

    private void calculateNetTotal() {
            double netTotal = 0.0;
            for (int i = 0; i < tblOrderDetails.getItems().size(); i++) {
                double total  = (double) colTotal.getCellData(i);
                netTotal += total;
            }
            lblTotal.setText(String.valueOf(netTotal));
        }
        
    private void setRemoveBtnOnAction(Button btn) {
        btn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (result.orElse(no) == yes) {

                int index = tblOrderDetails.getSelectionModel().getSelectedIndex();
                obList.remove(index);

                tblOrderDetails.refresh();
                //calculateNetTotal();
            }

        });
    }

    public void cmbItemIdOnAction(ActionEvent event) {
        String itemId = String.valueOf(cmbItemId.getSelectionModel().getSelectedItem());
        try {
            ItemStock stock = ItemStock.searchById(itemId);
            txtItemName.setText(String.valueOf(stock.getItem_Name()));
            txtnitPrice.setText(String.valueOf(stock.getUnitPrice()));
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) throws SQLException {

        String custId = String.valueOf(cmbCustomeId.getValue());
        String  itemId = String.valueOf(cmbItemId.getValue());
        String itemName = txtItemName.getText();
        double qty = Double.parseDouble(txtnitPrice.getText());
        double unitPrice = Double.parseDouble(txtnitPrice.getText());
        double total = Double.parseDouble(lblTotal.getText());
        String orderId = lblOrdrId.getText();
        double totalAmount = Double.parseDouble(lblTotal.getText());

        List<Cart> cartist = new ArrayList<>();

        for (int i = 0; i < tblOrderDetails.getItems().size(); i++) {
            OrderTm tm = obList.get(i);

            Cart cart = new Cart(tm.getItemId(), tm.getQty(), tm.getTotal(),tm.getBtn());
            cartist.add(cart);
        }
        boolean isSave = OrderModel.placeOrder(cartist,orderId,itemName,unitPrice,custId,sqlDate,totalAmount);
        if(isSave){
            new Alert(Alert.AlertType.INFORMATION,"Place Order Successful :)").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"something happened :(").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setCmb();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        visualize();
        DisplayDate();
        generateNextOrderId();
    }
        public void visualize(){
            colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
            colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    public void reportOrder(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\Order_all_fprm.jrxml"));

                    JasperReport js = JasperCompileManager.compileReport(load);

                    JasperPrint jp = JasperFillManager.fillReport(js, null,DBConnection.getInstance().getConnection());

                    JasperViewer viewer = new JasperViewer(jp , false);
                    viewer.show();

                } catch (JRException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }

    public void btnorderDetails(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("/view/Order_details_form.fxml");
            assert resource != null;
           Parent load = FXMLLoader.load(resource);
        anchorpayn1.getChildren().clear();
        anchorpayn1.getChildren().add(load);

    }
}


