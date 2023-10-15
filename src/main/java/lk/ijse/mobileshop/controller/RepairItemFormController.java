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
import lk.ijse.mobileshop.dto.RepairItem;
import lk.ijse.mobileshop.model.RepairItemModel;
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

public class RepairItemFormController implements Initializable {

    public TableColumn colRepairId;
    public TableColumn colCustId;
    public TableColumn colContact;
    public TableColumn colItemId;
    public TableColumn colDes;
    public TableColumn colPayment;
    public TableView tblrepair;

    @FXML
    private JFXTextField txtCustid;

    @FXML
    private JFXTextField txtItemid;

    @FXML
    private JFXTextField txtRepairId;

    @FXML
    private JFXTextField txtRepaircontact;

    @FXML
    private JFXTextField txtRepairdiscription;

    @FXML
    private JFXTextField txtRepairpayment;

    @FXML
    void RepairDelete(ActionEvent event) throws SQLException {
            Optional<ButtonType> are_you_sure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
            if(are_you_sure.isPresent()){
                if(are_you_sure.get().equals(ButtonType.YES)){
                    boolean delete = RepairItemModel.delete(txtRepairId.getText());
                    if(delete){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted Successful :)").show();
                        return;
                    }
                }
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();

            }
        setData();
    }

    @FXML
    void RepairSave(ActionEvent event) throws SQLException {
        if(!isValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Fields Correctly !",
                    ButtonType.OK
            ).show();
            return;
        }
        boolean isCreate = false;
        try {
            isCreate = RepairItemModel.saveRepairItem(correctData());
            if (isCreate) {
                new Alert(Alert.AlertType.CONFIRMATION, "Save repair_item Successfully...");
                generateNexttxtRepairItemId();
                txtCustid.clear();
                txtItemid.clear();
                txtRepairpayment.clear();
                txtRepaircontact.clear();
                txtRepairdiscription.clear();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        setData();
    }
    @FXML
    void RepairUpdate(ActionEvent event) throws SQLException{
        try {
            boolean update = RepairItemModel.update(correctData());
            if(update){
                new Alert(Alert.AlertType.INFORMATION,"Update Successful :)").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Update Failed :(").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setData();
    }

    @FXML
    void btnSearch(ActionEvent event) throws SQLException {
        RepairItem search = RepairItemModel.search(txtRepairId.getText());
        if(search!=null)
            set(search);
    }

    private void generateNexttxtRepairItemId() {
        try {
            String nextId = RepairItemModel.generateNextRepairItem_Id();
            txtRepairId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setTextFieldValidation(){
        validationutil.setFocus(txtRepairId,validationutil.RepIdPattern);
        validationutil.setFocus( txtCustid,validationutil.CustIdPAttern);
        validationutil.setFocus(txtItemid,validationutil.ItemIdPAttern);
        validationutil.setFocus(txtRepairpayment,validationutil.SallaryPAttern);
        validationutil.setFocus(txtRepaircontact,validationutil.contactPattern);
    }
    private boolean isValidated(){
        if(txtRepairId.getFocusColor().equals(Paint.valueOf("red")) ||
                txtCustid.getFocusColor().equals(Paint.valueOf("red")) ||
                txtItemid.getFocusColor().equals(Paint.valueOf("red"))||
                txtRepairpayment.getFocusColor().equals(Paint.valueOf("red"))||
                txtRepaircontact.getFocusColor().equals(Paint.valueOf("red"))

        ){
            return false;
        }else if(txtRepairId.getText().equals("") ||
                txtCustid.getText().equals("") ||
                txtItemid.getText().equals("")||
                txtRepairpayment.getText().equals("")||
                txtRepaircontact.getText().equals("")
        ){
            return false;
        }
        return true;
    }
    public void visualize(){
        colRepairId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("Cust_id"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("Item_id"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("Payment"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colDes.setCellValueFactory(new PropertyValueFactory<>("Discription"));
    }
    public void setData() throws SQLException {
        ObservableList<RepairItem> items = tblrepair.getItems();
        items.clear();
        ResultSet all = RepairItemModel.getAll();
        while (all.next()){
            items.add(new RepairItem(all.getString(1),all.getString(2),all.getString(3),all.getDouble(4)
            ,all.getString(5),all.getString(6)));
        }
    }

    private RepairItem correctData() {
        String id = txtRepairId.getText();
        String Custid = txtCustid.getText();
        String Itemid = txtItemid.getText();
        double Payment = Double.parseDouble(txtRepairpayment.getText());
        String Contact = txtRepaircontact.getText();
        String Discription = txtRepairdiscription.getText();
        return new RepairItem(id, Custid, Itemid,Payment, Contact,  Discription);
    }

        public void set(RepairItem ob){
            txtCustid.setText(ob.getCust_id());
            txtItemid.setText(ob.getItem_id());
            txtRepairpayment.setText(String.valueOf(ob.getPayment()));
            txtRepaircontact.setText(String.valueOf(ob.getContact()));
            txtRepairdiscription.setText(ob.getDiscription());
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
        generateNexttxtRepairItemId();
    }

    public void btnReport(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\Repair_item_all_form.jrxml"));

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

