package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXTextField;
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
import lk.ijse.mobileshop.dto.Customer;
import lk.ijse.mobileshop.model.CustomerModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    public JFXTextField txtCusId;
    public TableColumn colCustomerId1;
    public TableColumn colCusName1;
    public TableColumn colCusContact1;
    public TableView tblCustomers1;
    public TableColumn colCusAddress1;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private JFXTextField txtCusAddress;

    @FXML
    private JFXTextField txtCusContact;

    @FXML
    private JFXTextField txtCusName;

    @FXML
    void CusUpdate(ActionEvent event)throws SQLException {
        try {
            boolean update = CustomerModel.update(correctData());
            if(update){
                new Alert(Alert.AlertType.INFORMATION,"Update Successful :)").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Update Failed :(").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        generateNexttxtCusId();
        setData();
    }

    @FXML
    void btnSearch(ActionEvent event)throws SQLException {
        Customer search = CustomerModel.search(txtCusId.getText());
        if(search!=null)
            set(search);
    }
    @FXML
    void CusDelete(ActionEvent event)throws SQLException {
        Optional<ButtonType> are_you_sure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if(are_you_sure.isPresent()){
            if(are_you_sure.get().equals(ButtonType.YES)){
                boolean delete = CustomerModel.delete(txtCusId.getText());
                if(delete){
                    new Alert(Alert.AlertType.INFORMATION,"Deleted Successful :)").show();
                    return;
                }
            }
            new Alert(Alert.AlertType.ERROR,"Delete Failed").show();

        }
        setData();
        generateNexttxtCusId();
    }

    @FXML
    void CusSave(ActionEvent event) throws SQLException {
        if(!isValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Fields Correctly !",
                    ButtonType.OK
            ).show();
            return;
        }
        boolean isCreate = false;
        try {
            isCreate = CustomerModel.saveCustomer(correctData());
            if (isCreate) {
                new Alert(Alert.AlertType.CONFIRMATION, "Save Customer Successfully...");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        generateNexttxtCusId();
        txtCusName.clear();
        txtCusAddress.clear();
        txtCusContact.clear();
        setData();
    }
    public void setTextFieldValidation(){
        validationutil.setFocus(txtCusId,validationutil.CustIdPAttern);
        validationutil.setFocus( txtCusName,validationutil.namePattern);
        validationutil.setFocus(txtCusAddress,validationutil.AddressPAttern);
        validationutil.setFocus(txtCusContact,validationutil.contactPattern);
    }
    private boolean isValidated(){
        if(txtCusId.getFocusColor().equals(Paint.valueOf("red")) ||
                txtCusName.getFocusColor().equals(Paint.valueOf("red")) ||
                txtCusAddress.getFocusColor().equals(Paint.valueOf("red"))||
                txtCusContact.getFocusColor().equals(Paint.valueOf("red"))

        ){
            return false;
        }else if(txtCusId.getText().equals("") ||
                txtCusName.getText().equals("") ||
                txtCusAddress.getText().equals("")||
                txtCusContact.getText().equals("")
        ){
            return false;
        }
        return true;
    }
    public void set(Customer ob){
        txtCusName.setText(ob.getName());
        txtCusAddress.setText(String.valueOf(ob.getAddress()));
        txtCusContact.setText(String.valueOf(ob.getContact()));
    }

    @FXML
    void txtKeyPress(KeyEvent event) {

    }

    public void visualize(){
        colCustomerId1.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusAddress1.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCusContact1.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    public void setData() throws SQLException {
        ObservableList<Customer> items = tblCustomers1.getItems();
        ResultSet all = CustomerModel.getAll();
        while (all.next()){
            items.add(new Customer(all.getString(1),all.getString(2),all.getString(3),all.getString(4)));
        }
    }


    private Customer correctData() {
        String id = txtCusId.getText();
        String name = txtCusName.getText();
        String address = txtCusAddress.getText();
        String contact = txtCusContact.getText();
        return new Customer(id, name,address,contact);
    }

    public void setTable(){

    }
    private void generateNexttxtCusId() {
        try {
            String nextId = CustomerModel.generateNextCust_Id();
            txtCusId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        generateNexttxtCusId();
    }

    public void cusPrint(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\customer_all_form.jrxml"));

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
}


