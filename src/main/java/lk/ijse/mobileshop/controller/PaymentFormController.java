package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import lk.ijse.mobileshop.Util.validationutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.tm.PaymentTm;
import lk.ijse.mobileshop.dto.Payment;
import lk.ijse.mobileshop.model.PaymentModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentFormController implements Initializable {

    public TableView tblPayment;
    public TableColumn colPaymentId;
    public TableColumn colOrderId;
    public TableColumn colCustId;
    public TableColumn colDate;
    public TableColumn colRepairId;
    public TableColumn colPrice;
    public JFXButton txtSave;

    @FXML
    private JFXTextField txtCustid;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtOrderid;

    @FXML
    private JFXTextField txtPaymentid;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtRepairid;

    @FXML
    void PaymentDelete(ActionEvent event) throws SQLException {
            Optional<ButtonType> are_you_sure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
            if(are_you_sure.isPresent()){
                if(are_you_sure.get().equals(ButtonType.YES)){
                    boolean delete = PaymentModel.delete(txtPaymentid.getText());
                    if(delete){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted Successful :)").show();
                        return;
                    }
                }
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();

            }
        setData();
        generateNexttxtPaymentid();
    }

    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

    @FXML
    void PaymentUpdate(ActionEvent event) throws SQLException {
        try {
            boolean update = PaymentModel.update(correctData());
            if(update){
                new Alert(Alert.AlertType.INFORMATION,"Update Successful :)").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Update Failed :(").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setData();
        generateNexttxtPaymentid();
    }

    @FXML
    void btnSearch(ActionEvent event)throws SQLException {
        Payment search = PaymentModel.search(txtPaymentid.getText());
        if(search!=null)
            set(search);
    }
    public void PaymentSave(ActionEvent actionEvent) throws SQLException {
        if(!isValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Fields Correctly !",
                    ButtonType.OK
            ).show();
            return;
        }
        boolean isCreate = false;
        try {
            isCreate = PaymentModel.savePayment(correctData());
            if (isCreate) {
                new Alert(Alert.AlertType.CONFIRMATION, "Save Payment Successfully...");
                generateNexttxtPaymentid();
                txtCustid.clear();
                txtOrderid.clear();
                txtRepairid.clear();
                txtDate.clear();
                txtPrice.clear();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setData();
        generateNexttxtPaymentid();
    }

    private void generateNexttxtPaymentid() {
        try {
            String nextId = PaymentModel.generateNextPay_Id();
            txtPaymentid.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setTextFieldValidation(){
        validationutil.setFocus(txtPaymentid,validationutil.PayIdPAttern);
        validationutil.setFocus( txtCustid,validationutil.CustIdPAttern);
        validationutil.setFocus(txtOrderid,validationutil.OderIdPAttern);
        validationutil.setFocus(txtRepairid,validationutil.RepIdPattern);
        validationutil.setFocus(txtPrice,validationutil.SallaryPAttern);
    }
    private boolean isValidated(){
        if(txtPaymentid.getFocusColor().equals(Paint.valueOf("red")) ||
                txtCustid.getFocusColor().equals(Paint.valueOf("red")) ||
                txtOrderid.getFocusColor().equals(Paint.valueOf("red"))||
                txtRepairid.getFocusColor().equals(Paint.valueOf("red"))||
                txtPrice.getFocusColor().equals(Paint.valueOf("red"))
        ){
            return false;
        }else if(txtPaymentid.getText().equals("") ||
                txtCustid.getText().equals("") ||
                txtOrderid.getText().equals("")||
                txtRepairid.getText().equals("")||
                 txtPrice.getText().equals("")
        ){
            return false;
        }
        return true;
    }
    public void visualize(){
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("Pay_id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("Oder_id"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("Cust_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Rep_id"));
        colRepairId.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Total_Price"));

    }

    public void setData() throws SQLException {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();
        List<Payment> list = PaymentModel.getAll();

        for(Payment payment : list){
            obList.add(new PaymentTm(
                    payment.getPay_id(),
                    payment.getOder_id(),
                    payment.getCust_id(),
                    payment.getRep_id(),
                    payment.getDate(),
                    payment.getTotal_Price()
            ));
        }
        tblPayment.setItems(obList);
    }
    private Payment correctData() {
        String Pay_id = txtPaymentid.getText();
        String Cust_id = txtCustid.getText();
        String orderId = txtOrderid.getText();
        String Rep_id = txtRepairid.getText();
        double Total_Price = Double.parseDouble(txtPrice.getText());
        return new Payment(Pay_id,Cust_id,orderId,Rep_id,sqlDate,Total_Price);
    }

        public void set(Payment ob){
            txtCustid.setText(ob.getCust_id());
            txtOrderid.setText(ob.getOder_id());
            txtRepairid.setText(ob.getRep_id());
            txtDate.setText(String.valueOf(sqlDate));
            txtPrice.setText(String.valueOf(ob.getTotal_Price()));
        }


    @FXML
    void txtKeyPress(KeyEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFieldValidation();
        visualize();
        try {
            setData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        generateNexttxtPaymentid();
    }

    public void btnprint(ActionEvent actionEvent) {
                new Thread() {
                    private dbconnection DBConnection;
                    @Override
                    public void run() {
                        try {
                            JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\payment_all_form.jrxml"));

                            JasperReport js = JasperCompileManager.compileReport(load);

                            JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());

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
}
