package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXButton;
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
import lk.ijse.mobileshop.dto.Employee;
import lk.ijse.mobileshop.model.EmployeeModel;
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

public class EmployeeFormController implements Initializable {

    public TableColumn colSalary;
    @FXML
    private TableColumn<?, ?> colCusName;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableView<Employee> tblCustomers;

    @FXML
    private JFXTextField txtEmpId;

    @FXML
    private JFXTextField txtEmpName;

    @FXML
    private JFXTextField txtEmpSallary;

    @FXML
    private JFXButton txtEmpdelete;

    @FXML
    private JFXButton txtEmpsave;

    @FXML
    private JFXButton txtEmpupdate;

    @FXML
    void EmpSave(ActionEvent event) throws SQLException {
        if(!isValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Fields Correctly !",
                    ButtonType.OK
            ).show();
            return;
        }
        boolean isCreate = false;
        try {
            isCreate = EmployeeModel.saveEmployee(correctData());
            if (isCreate) {
                new Alert(Alert.AlertType.CONFIRMATION, "Save Employee Successfully...");
                generateNexttxtEmpId();
                txtEmpName.clear();
                txtEmpSallary.clear();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setData();
        generateNexttxtEmpId();

    }
    @FXML
    void Empdelete(ActionEvent event)throws SQLException {
        Optional<ButtonType> are_you_sure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if(are_you_sure.isPresent()){
            if(are_you_sure.get().equals(ButtonType.YES)){
                boolean delete = EmployeeModel.delete(txtEmpId.getText());
                if(delete){
                    new Alert(Alert.AlertType.INFORMATION,"Deleted Successful :)").show();
                    return;
                }
            }
            new Alert(Alert.AlertType.ERROR,"Delete Failed").show();

        }
        setData();
        generateNexttxtEmpId();
    }

    @FXML
    void Empupdate(ActionEvent event) throws SQLException {
        try {
            boolean update = EmployeeModel.update(correctData());
            if(update){
                new Alert(Alert.AlertType.INFORMATION,"Update Successful :)").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Update Failed :(").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setData();
        generateNexttxtEmpId();
    }

    @FXML
    void btnSearch(ActionEvent event) throws SQLException {
        Employee search = EmployeeModel.search(txtEmpId.getText());
        if(search!=null)
            set(search);
    }

    public void setTextFieldValidation(){
        validationutil.setFocus(txtEmpId,validationutil.EmpIdPAttern);
        validationutil.setFocus( txtEmpName,validationutil.namePattern);
        validationutil.setFocus(txtEmpSallary,validationutil.SallaryPAttern);
    }
    private boolean isValidated(){
        if(txtEmpId.getFocusColor().equals(Paint.valueOf("red")) ||
                txtEmpName.getFocusColor().equals(Paint.valueOf("red")) ||
                txtEmpSallary.getFocusColor().equals(Paint.valueOf("red"))

        ){
            return false;
        }else if(txtEmpId.getText().equals("") ||
                txtEmpName.getText().equals("") ||
                txtEmpSallary.getText().equals("")
        ){
            return false;
        }
        return true;
    }

    public void visualize(){
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("sallary"));
    }

    public void setData() throws SQLException {
        ObservableList<Employee> items = tblCustomers.getItems();
        ResultSet all = EmployeeModel.getAll();
        while (all.next()){
            items.add(new Employee(all.getString(1),all.getString(2),all.getDouble(3)));
        }
    }

    private void generateNexttxtEmpId() {
        try {
            String nextId = EmployeeModel.generateNextId();
            txtEmpId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Employee correctData() {
        String id = txtEmpId.getText();
        String name = txtEmpName.getText();
        double sallary = Double.parseDouble(txtEmpSallary.getText());
        return new Employee(id, name,sallary);
    }



        public void set(Employee ob){
            txtEmpName.setText(ob.getName());
            txtEmpSallary.setText(String.valueOf(ob.getSallary()));
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
        generateNexttxtEmpId();
    }

    public void empPrint(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;

            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\employee_all_form.jrxml"));

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
            }}.start();
    }
}
