package lk.ijse.mobileshop.controller;

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
import lk.ijse.mobileshop.dto.Supplier;
import lk.ijse.mobileshop.model.SupplierModel;
import lk.ijse.mobileshop.tm.SupplierTm;
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

public class SupplierFormController implements Initializable {


    public TableColumn colsupid;
    public TableColumn colsupdate;
    public TableColumn colsupitem;
    public TableView tbisupplier;

    @FXML
    private JFXTextField txtSItem;



    @FXML
    private JFXTextField txtSId;

    @FXML
    private JFXTextField txtSDate;

    Date date = new Date();
    java.sql.Date sqlDate = new java.sql.Date(date.getTime());


    @FXML
    void SupKeyPress(KeyEvent event) {

    }

    @FXML
    void btnSearchCus(ActionEvent event) throws SQLException {
            Supplier search = SupplierModel.search(txtSId.getText());
            if(search!=null)
                set(search);
        }
        public void set(Supplier ob){
            txtSDate.setText(String.valueOf(ob.getDate()));
            txtSItem.setText(ob.getItem());
        }

    public void btnupdate(ActionEvent actionEvent) throws SQLException {
        try {
            boolean update = SupplierModel.update(correctData());
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

    public void btnsave(ActionEvent actionEvent) throws SQLException {
        if(!isValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Fields Correctly !",
                    ButtonType.OK
            ).show();
            return;
        }
        boolean isCreate = false;
        try {
            isCreate = SupplierModel.saveSupplier(correctData());
            if (isCreate) {
                new Alert(Alert.AlertType.CONFIRMATION, " Supplier Save Successfully...");



            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        generateNexttxtSid();
        txtSDate.clear();
        txtSItem.clear();
        setData();

    }
    public void btndelete(ActionEvent actionEvent) throws SQLException {
        Optional<ButtonType> are_you_sure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if(are_you_sure.isPresent()){
            if(are_you_sure.get().equals(ButtonType.YES)){
                boolean delete = SupplierModel.delete(txtSId.getText());
                if(delete){
                    new Alert(Alert.AlertType.INFORMATION,"Deleted Success :)").show();
                    return;
                }
            }
            new Alert(Alert.AlertType.ERROR,"Delete Failed").show();

        }
        setData();
    }


    private void generateNexttxtSid() {
        try {
            String nextId = SupplierModel.generateNextSup_Id();
            txtSId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setTextFieldValidation(){
        validationutil.setFocus(txtSId,validationutil.SupIdPAttern);
    }
    private boolean isValidated(){
        if(txtSId.getFocusColor().equals(Paint.valueOf("red"))

        ){
            return false;
        }else if(txtSId.getText().equals("")
        ){
            return false;
        }
        return true;
    }
    public void visualize(){
        colsupid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colsupdate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colsupitem.setCellValueFactory(new PropertyValueFactory<>("Item"));

    }

    public void setData()throws SQLException {
        ObservableList<SupplierTm>obList = FXCollections.observableArrayList();
        List<Supplier> list = SupplierModel.getAll();

        for(Supplier supplier : list){
            obList.add(new SupplierTm(
                    supplier.getId(),
                    supplier.getDate(),
                    supplier.getItem()

            ));

            ;
        }
        tbisupplier.setItems(obList);
    }

    private Supplier correctData() {
        String id = txtSId.getText();
        String Item = txtSItem.getText();
        return new Supplier(id, sqlDate, Item);
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
        generateNexttxtSid();
    }

    public void btnRrport(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\supplier_all_form.jrxml"));

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
